import java.io.Serializable;


public interface AccessControlContainer {

	boolean HasAccess(int msgId, Serializable[] load, String username);

}
