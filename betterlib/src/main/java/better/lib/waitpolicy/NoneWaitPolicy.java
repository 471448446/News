package better.lib.waitpolicy;

/**
 * 没有请求提示，静默请求。
 *
 */
public class NoneWaitPolicy implements WaitPolicy {

	@Override
	public void displayLoading(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayLoading() {

	}

	@Override
	public void displayRetry(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayRetry() {

	}

	@Override
	public void disappear() {
		// TODO Auto-generated method stub
		
	}

}
