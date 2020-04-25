package com.gumirov.gav.mailerdsl;

public class Test {
    public static void main(String []args){
        System.out.println("Start:");
        MailerDSL classUnderTest = new MailerDSL();
        System.out.println(classUnderTest.init("https://127.0.0.1:443", "secret"));
        String uid = classUnderTest.send("andreyt45@gmail.com", "subject sad !#$@#$&^%&", "TEST in junit");
        System.out.println("Uid: "+uid);
        try {
            System.out.println(classUnderTest.isOK(uid));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
