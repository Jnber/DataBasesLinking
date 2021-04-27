package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
	/*Receiver r = new Receiver();*/
	JdbcRetrieve j = new JdbcRetrieve("bo1");
	j.connection();
	Object[][] obj=j.getArray();
	/*r.receive();*/
		FrameBO bo = new FrameBO(j);
    }
}
