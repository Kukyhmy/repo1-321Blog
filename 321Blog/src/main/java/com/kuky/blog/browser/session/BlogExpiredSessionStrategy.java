/**
 * 
 */
package com.kuky.blog.browser.session;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;


public class BlogExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

	public BlogExpiredSessionStrategy(String invalidSessionUrl) {
		super(invalidSessionUrl);
	}


	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		onSessionInvalid(event.getRequest(), event.getResponse());
	}
	

	@Override
	protected boolean isConcurrency() {
		return true;
	}

}
