package net.rickiekarp.reddit.captcha;

public class CaptchaException extends Exception {
	static final long serialVersionUID = 40;
	
	public CaptchaException(String message) {
		super(message);
	}
}
