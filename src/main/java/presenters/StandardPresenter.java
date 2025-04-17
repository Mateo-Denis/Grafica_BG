package presenters;

import views.IToggleableView;

public abstract class StandardPresenter {

	protected IToggleableView view;
	public void start(){
		view.setPresenter(this);
		initListeners();
	}
	protected abstract void initListeners();

}
