package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        
        HomeFrame home = new HomeFrame();

        while( home.getNbOfSenders() == 0 ){ System.out.println(home.getNbOfSenders());}
        int nbOfSenders = home.getNbOfSenders();

        ArrayList<FrameBO> users = new ArrayList<FrameBO>();

        for(int i=1; i<nbOfSenders+1; i++){
            JdbcRetrieve j = new JdbcRetrieve("bo"+i);
            j.connection();
            users.add(new FrameBO(j,i));  }

}}
