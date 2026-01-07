/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.sajiwa.controller;
import id.ac.unpas.sajiwa.view.RegisterView;
import id.ac.unpas.sajiwa.view.LoginView;

/**
 *
 * @author Fitriyani Rahmadini
 */
public class RegisterController {
      
    private RegisterView view;
    
    public RegisterController(RegisterView view) {
        this.view = view;
        
        // Listener Tombol Kembali
        view.btnBack.addActionListener(e -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView); // Pasang controller
            loginView.setVisible(true);
            view.dispose();
        });
    }
    
}
