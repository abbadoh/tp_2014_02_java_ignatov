package frontend;

import MessageSystem.Address;
import MessageSystem.AddressService;

/**
 * Created by gumo on 04/04/14.
 */
public class UserSession {
    private Address accountService;

    private String name;
    private String sessionId;
    private Long userId;
    private Integer registred;
    private Boolean serverIsDown;

    public UserSession(String sessionId, String name, AddressService addressService) {
        this.sessionId = sessionId;
        this.name = name;
        this.accountService = addressService.getDatabaseService();
        this.serverIsDown = false;
    }

    public UserSession(String sessionId,AddressService addressService) {
        this.sessionId = sessionId;
        this.accountService = addressService.getDatabaseService();
        this.registred = 0;
        this.serverIsDown = false;
    }

    public Address getAccountService() {
        return accountService;
    }

    public String getName(){
        return name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getRegState() { return this.registred;}

    public Boolean getServerState(){  return serverIsDown; }

    public void setServerState(Boolean state) { this.serverIsDown = state;}

    public void setName(String name) { this.name = name; }

    public void setUserId(Long userId) {
        this.userId = userId;
        this.registred = 1;
    }

    public void setRegState(Integer state) { this.registred = state; }

}
