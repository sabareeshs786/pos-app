package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class SchedulerForm {

	private LocalDateTime startDate;
	private LocalDateTime endDate;
}