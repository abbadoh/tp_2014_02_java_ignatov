package MessageSystem;

/**
 * Created by gumo on 03/04/14.
 */
public class AddressService {
    private Address databaseService1;
    private Address databaseService2;
    private Address frontend;
    private Integer system;

    public AddressService(){
        this.system = 0;
    }
    public Address getDatabaseService() {
        if(system == 1) {
            system = 2;
            return databaseService1;
        }
        else {
            system = 1;
            return databaseService2;
        }
    }

    public Address getFrontend(){
        return frontend;
    }

    public void setDatabaseService(Address databaseService) {
        if(system == 0) {
            this.databaseService1 = databaseService;
            ++system;
        }
        if(system == 1) {
            this.databaseService2 = databaseService;
        }
    }

    public void setFrontend(Address frontend) {
        this.frontend = frontend;
    }


}
