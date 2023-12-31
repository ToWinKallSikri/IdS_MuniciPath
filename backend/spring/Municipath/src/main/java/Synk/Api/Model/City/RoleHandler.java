package Synk.Api.Model.City;

import Synk.Api.Model.MuniciPathMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleHandler {
	private List<Licence> authorizations;
	private List<RoleRequest> requests;
	public RoleHandler() {
		authorizations = new ArrayList<Licence>();
		requests = new ArrayList<RoleRequest>();
	}
	public Licence getAuthorization(String username, String cityId) {
		MuniciPathMediator mediator = new MuniciPathMediator();
		if(mediator.usernameExists(username)) {
			return null;
		} else {
			return authorizations.stream().parallel()
					.filter(l -> l.getCityId().equals(cityId) && l.getUsername().equals(username))
					.findFirst().orElse(new Licence(username,cityId, Role.TOURIST));
		}
	}

	public List<Licence> getAuthorizations(String cityId) {
		return authorizations.stream().parallel()
				.filter(l -> l.getCityId().equals(cityId))
				.collect(Collectors.toList());
	}

	public boolean judge(String requestId, boolean outcome) {
		if(!checkRequest(requestId)) {
			return false;
		} if (outcome){
			requests.stream().parallel()
					.filter(r -> r.getRequestId().equals(requestId))
					.findFirst().ifPresent(r -> authorizations.add(new Licence(r.getUsername(),r.getCityId(), ->
					switch (getAuthorization().getRole()) {
						case CONTR_NOT_AUTH -> Role.CONTR_AUTH;
						case TOURIST -> Role.CONTR_NOT_AUTH;
						default -> Role.TOURIST;
					}));
		}
		removeRequest(requestId);
		return true;

	}

	private Optional<RoleRequest> checkRequest (String requestId) {
		return requests.stream().parallel()
				.anyMatch(r -> r.getRequestId().equals(requestId));
	}

	private void removeRequest (String requestId) {
		requests.stream().parallel()
				.filter(r -> r.getRequestId().equals(requestId))
				.findFirst().ifPresent(r -> requests.remove(r));
	}

	public boolean addRequest(String cityId,String username) {
		MuniciPathMediator m1 = new MuniciPathMediator();
		if(!m1.usernameExists(username)) {
			return false;
		} else {
			String requestId = "" + (cityId+username).hashCode();
			RoleRequest request = new RoleRequest(cityId, username, requestId);
			requests.add(request);
			return true;
		}
	}

	public boolean setRole(String username, String cityId, Role role) {
		MuniciPathMediator m1 = new MuniciPathMediator();
		if(!m1.usernameExists(username)) {
			return false;
		} else {
			authorizations.stream().parallel()
					.filter(l -> l.getCityId().equals(cityId) && l.getUsername().equals(username))
					.findFirst().ifPresent(l -> l.setRole(role));
			return true;
		}
	}

}
