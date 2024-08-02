package presenters;

import views.IToggleableView;
import views.ToggleableView;

public abstract class StandardPresenter {

	protected IToggleableView view;
	public void start(){
		view.setPresenter(this);
		initListeners();
	}
	protected abstract void initListeners();

}
