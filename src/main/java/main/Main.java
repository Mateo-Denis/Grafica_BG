package main;

import presenters.HomePresenter;
import views.home.IHomeView;
import views.home.HomeView;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {

		//IHomeView HomeView = new HomeView();
		//30-7-2024 SE MODIFICA ESTO:
		HomeView home = new HomeView();
		new HomePresenter(home);
		home.setVisible(true);
		//FIN DEL AGREGADO

	}
}