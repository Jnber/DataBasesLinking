package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
	Receiver r = new Receiver();
	JdbcRetrieve j = new JdbcRetrieve();
	j.retrieve();
	r.receive();
    }
}
