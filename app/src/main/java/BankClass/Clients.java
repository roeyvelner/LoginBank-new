package BankClass;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 03/12/2017.
 */

public class Clients {
    private LinkedList <Client> clientList ;

    public Clients(){
        clientList = new LinkedList<>();
    }

    public void addClient(Client client){
        clientList.add(client);
    }

    public Client getClient(String userName,String Password){
        for (int i=0;i<clientList.size();i++){
            if (clientList.get(i).getEmail()!= null) {
                if (clientList.get(i).getEmail().compareTo(userName) == 0) {
                    return clientList.get(i);
                }
            }
        }
        return  null;
    }

    public Client getClient(String userName){
        for (int i=0;i<clientList.size();i++){
            if (clientList.get(i).getEmail()!= null) {
                if (clientList.get(i).getEmail().compareTo(userName) == 0) {
                    return clientList.get(i);
                }
            }
        }
        return  null;
    }

    public int getClientindex(String userName){
        for (int i=0;i<clientList.size();i++){
            if (clientList.get(i).getEmail()!= null) {
                if (clientList.get(i).getEmail().compareTo(userName) == 0) {
                    return i;
                }
            }
        }
        return  -1;
    }

    public int getClientindex(String userName,String Password){
        for (int i=0;i<clientList.size();i++){
            if (clientList.get(i).getEmail()!= null) {
                if (clientList.get(i).getEmail().compareTo(userName) == 0) {
                    return i;
                } else
                    return -1;
            }
        }
        return  -1;
    }

    public int getClientindex(Client client){
        return clientList.indexOf(client);
    }

    public void setClient(Client client){
        int saveIndex= getClientindex(client.getEmail());
        if (saveIndex!=-1)
            clientList.set(saveIndex,client);
    }
    public Client GetClientByIndex(int index){
        return clientList.get(index);
    }
    public void setClient(int index,Client c){
        clientList.set(index,c);
    }
}
