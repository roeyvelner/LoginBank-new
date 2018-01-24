package BankClass;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by User on 03/12/2017.
 */

public class Client {
    private String firstName;
    private String lastName;
    private String CityAdress;
    private String StreetAdress;
    private String Email;
    private String VerificationQuestion;
    private String VerificationAnswer;
    private String picturePath;
    private String job;
    private Firebase firebase;

    public Client(){

    }
    public Client(Client client){
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.CityAdress = client.getCityAdress();
        this.StreetAdress = client.getStreetAdress();
        this.Email = client.getEmail();
        this.VerificationAnswer = client.getVerificationAnswer();
        this.VerificationQuestion = client.getVerificationQuestion();
        this.picturePath = client.getpicturePath();
        this.job = client.getJob();
    }

    public Client(String firstName, String lastName,String CityAdress,String StreetAdress,String Email,String VerificationAnswer , String VerificationQuestion,String picturePath,String job){
        firebase =new Firebase("https://bank-login.firebaseio.com/Users");
        this.firstName = firstName;
        addClientToFireBase("firstName",firstName);
        this.lastName = lastName;
        addClientToFireBase("lastName",lastName);
        this.CityAdress = CityAdress;
        addClientToFireBase("CityAdress",CityAdress);
        this.StreetAdress = StreetAdress;
        addClientToFireBase("StreetAdress",StreetAdress);
        this.Email = Email;
        addClientToFireBase("Email",Email);
        this.VerificationAnswer = VerificationAnswer;
        addClientToFireBase("VerificationAnswer",VerificationAnswer);
        this.VerificationQuestion = VerificationQuestion;
        addClientToFireBase("VerificationQuestion",VerificationQuestion);
        this.picturePath = picturePath;
        addClientToFireBase("picturePath",picturePath);
        this.job=job;
        addClientToFireBase("job",job);
    }
    public Client(String firstName, String lastName,String CityAdress,String StreetAdress,String Email,String VerificationAnswer , String VerificationQuestion,String picturePath,String job,boolean flag){
        if (flag) firebase =new Firebase("https://bank-login.firebaseio.com/Users");
        this.firstName = firstName;
        if (flag) addClientToFireBase("firstName",firstName);
        this.lastName = lastName;
        if (flag) addClientToFireBase("lastName",lastName);
        this.CityAdress = CityAdress;
        if (flag) addClientToFireBase("CityAdress",CityAdress);
        this.StreetAdress = StreetAdress;
        if (flag) addClientToFireBase("StreetAdress",StreetAdress);
        this.Email = Email;
        if (flag) addClientToFireBase("Email",Email);
        this.VerificationAnswer = VerificationAnswer;
        if (flag) addClientToFireBase("VerificationAnswer",VerificationAnswer);
        this.VerificationQuestion = VerificationQuestion;
        if (flag) addClientToFireBase("VerificationQuestion",VerificationQuestion);
        this.picturePath = picturePath;
        if (flag) addClientToFireBase("picturePath",picturePath);
        this.job = job;
        if (flag) addClientToFireBase("job",job);
    }

    public void addClientToFireBaseInit(String nameOfChild){
        Firebase.ResultHandler Rsh = new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(FirebaseError firebaseError) {

            }
        };
        if (false) {
            firebase.createUser("roeyvelner@gmail.com", "1234", Rsh);
            firebase.child(nameOfChild).push();
            firebase = new Firebase("https://bank-login.firebaseio.com/Users/" + getEmail());
        }
    }
    public void addClientToFireBase(String nameOfChild,String Value){
        if (false) {


            firebase.child(nameOfChild).push();
            firebase = new Firebase("https://bank-login.firebaseio.com/Users/" + Email + "/" + nameOfChild);
            firebase.setValue(Value);
            firebase = new Firebase("https://bank-login.firebaseio.com/Users/" + Email);
        }
    }
    public void setClientToFireBase(String nameOfChild,String Value){
        if (false) {
            //firebase.child(nameOfChild).push();
            firebase = new Firebase("https://bank-login.firebaseio.com/Users/" + Email + "/" + nameOfChild);
            firebase.setValue(Value);
            firebase = new Firebase("https://bank-login.firebaseio.com/Users/" + Email);
        }
    }



    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getCityAdress(){
        return CityAdress;
    }
    public String getStreetAdress(){
        return StreetAdress;
    }
    public String getEmail(){
        return Email;
    }
    public String getVerificationQuestion(){
        return VerificationQuestion;
    }
    public String getVerificationAnswer(){
        return VerificationAnswer;
    }
    public String getpicturePath(){
        return picturePath;
    }
    public String getJob(){
        return job;
    }



    public void setFirstName(String firstName){
        this.firstName = firstName;
        setClientToFireBase("firstName",firstName);
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
        setClientToFireBase("lastName",lastName);
    }
    public void setCityAdress(String CityAdress){
        this.CityAdress = CityAdress;
        setClientToFireBase("CityAdress",CityAdress);
    }
    public void setStreetAdress(String StreetAdress){

        this.StreetAdress = StreetAdress;
        setClientToFireBase("StreetAdress",StreetAdress);

    }
    public void setEmail(String Email){
        this.Email = Email;
        setClientToFireBase("Email",Email);
    }
    public void setVerificationQuestion(String VerificationQuestion){
        this.VerificationQuestion = VerificationQuestion;
        setClientToFireBase("VerificationQuestion",VerificationQuestion);
    }
    public void setVerificationAnswer(String VerificationAnswer){
        this.VerificationAnswer = VerificationAnswer;
        setClientToFireBase("VerificationAnswer",VerificationAnswer);
    }
    public void setpicturePath(String picturePath){
        this.picturePath = picturePath;
        setClientToFireBase("picturePath",picturePath);
    }
    public void setJob(String job){
        this.job = job;
        setClientToFireBase("job",job);
    }

}