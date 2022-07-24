package com.kikawet.reactiveMediaServer.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import com.kikawet.reactiveMediaServer.dto.WatchedVideoDTO;

@Component
public class WatcherVideoDTOValidationHandler extends AbstactRequestValidationHandler<WatchedVideoDTO, Validator> {

	public WatcherVideoDTOValidationHandler(@Autowired Validator validator) {
		super(WatchedVideoDTO.class, validator);
	}

}
