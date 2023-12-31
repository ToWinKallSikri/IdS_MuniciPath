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
		Optional<RoleRequest> oRequest = getRequest(requestId);
		if(oRequest.isEmpty())
			return false;
		if (outcome){
			String user = oRequest.get().getUsername();
			String city = oRequest.get().getCityId();
			Role role = getAuthorization(user, city).getRole();
			role = switch (role) {
						case CONTR_NOT_AUTH -> Role.CONTR_AUTH;
						case TOURIST -> Role.CONTR_NOT_AUTH;
						default -> role;
			};
			setRole(user, city, role);
		}
		removeRequest(requestId);
		return true;

	}

	private Optional<RoleRequest> getRequest (String requestId) {
		return requests.stream().parallel()
				.filter(r -> r.getRequestId().equals(requestId))
				.findFirst();
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
			String requestId = cityId.hashCode() + "." + username.hashCode();
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
