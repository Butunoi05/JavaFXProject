package com.example.javafxprodus;

import Domain.Comanda;
import Domain.Produs;
import Repository.*;
import Service.ServiceComanda;
import Service.ServiceProdus;
import UI.Ui;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws RepositoryException, IOException, SQLException {
        Irepository<Produs> repoGeneric;
        Irepository<Comanda> comandaIrepository;
        Properties p = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("a.properties")) {
            if (is == null) {
                throw new IOException("Fisierul a.properties nu a fost gasit in classpath.");
            }
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String RepoType=p.getProperty("RepositoryProdus");
        String RepoTypeComanda=p.getProperty("RepositoryComanda");
        if("Memory".equals(RepoType))repoGeneric=new RepoGeneric<>();
        else if("DataBase".equals(RepoType))repoGeneric=new ProdusDBRepository();
        else {throw new RepositoryException("Eroare la settings");}


        if("Memory".equals(RepoTypeComanda))comandaIrepository=new RepoGeneric<>();
        else if("DataBase".equals(RepoType))comandaIrepository=new ComandaDBRepository();
        else {throw new RepositoryException("Eroare la settings");}

        ServiceProdus serviceProdus=new ServiceProdus(repoGeneric);
        ServiceComanda serviceComanda=new ServiceComanda(comandaIrepository);
        Ui ui=new Ui(serviceProdus,serviceComanda);
        ui.start();
    }
    }