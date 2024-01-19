package Synk.Api.Controller.Pending;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.Pending.PendingRepository;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.View.ViewModel.ProtoGroup;
import Synk.Api.View.ViewModel.ProtoPost;

@Service
public class PendingHandler {

	/**
	 * Oggetto mediator, utilizzato per far comunicare i vari handler fra di loro
	 */
	private MuniciPathMediator mediator;

	/**
	 * Oggetto pendingRepository, utilizzato per gestire la JPA
	 */
	@Autowired
	private PendingRepository pendingRepository;

	/**
	 * Oggetto IdentifierManager, utilizzato per gestire le logiche degli id
	 */
	private IdentifierManager idManager = new IdentifierManager();

	/**
	 * Metodo setter per l'oggetto mediator
	 * @param mediator il mediator da settare
	 */
	public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }

	/**
	 * Metodo per aggiungere una richiesta in pending
	 * @param id l'id della richiesta
	 */
	public void addRequest(String id) {
		if(id == null)
			return;
		this.pendingRepository.save(new PendingRequest(id));
	}

	/**
	 * Metodo per aggiungere un nuovo post in pending
	 * @param postId l'id del post
	 * @param data dati del post
	 */
	public void addPostRequest(String postId, ProtoPost data) {
		if(postId == null || data == null)
			return;
		this.pendingRepository.save(new PendingRequest(postId, data));
	}

	/**
	 * Metodo per aggiungere un nuovo gruppo in pending
	 * @param groupId l'id del gruppo
	 * @param data dati di un gruppo
	 */
	public void addGroupRequest(String groupId, ProtoGroup data) {
		if(groupId == null || data == null)
			return;
		this.pendingRepository.save(new PendingRequest(groupId, data));
	}

	/**
	 * Metodo per giudicare una richiesta in pending
	 * @param pendingId l'id della richiesta
	 * @param outcome l'esito della richiesta
	 * @param motivation eventuale motivazione in caso di giudizio negativo
	 * @return true se la richiesta è stata giudicata, false altrimenti
	 */
	public boolean judge(String pendingId, boolean outcome, String motivation) {
		if(pendingId == null || motivation == null)
			return false;
		PendingRequest request = getRequest(pendingId);
		if(request == null)
			return false;
		boolean isGroup = idManager.isGroup(pendingId);
		String username = this.mediator.getAuthor(pendingId);
		String response = "Contenuto " + (outcome ? "accettato" : "rifiutato") + ".\n";
		if(outcome) {
			if(isGroup) 
				mediator.manageGroupRequest(request);
			else mediator.managePostRequest(request);
		} else if(request.isNew()) {
			if(isGroup) 
				this.mediator.deletePendingGroup(pendingId);
			else this.mediator.deletePendingPost(pendingId);
		}
		String name = this.mediator.getNameOfCity(this.idManager.getCityId(pendingId));
		this.mediator.send(name ,pendingId, response+outcome, username);
		this.pendingRepository.delete(request);
		return true;
	}

	/**
	 * Metodo per ottenere tutte le richieste in pending di una determinata città
	 * @param cityId l'id della città di cui si vogliono ottenere le richieste
	 * @return una lista contenente tutte le richieste in pending della città
	 */
	public List<PendingRequest> getAllRequest(String cityId){
		if(cityId == null)
			return null;
		return StreamSupport.stream(pendingRepository.findAll().spliterator(), true)
				.filter(p -> idManager.getCityId(p.getId()).equals(cityId)).toList();
	}

	/**
	 * Metodo per ottenere una richiesta in pending
	 * @param requestId l'id della richiesta
	 * @return la richiesta se esiste, null altrimenti
	 */
	public PendingRequest getRequest(String requestId) {
		if(requestId == null)
			return null;
		return this.pendingRepository.findById(requestId).orElse(null);
	}



	
}
