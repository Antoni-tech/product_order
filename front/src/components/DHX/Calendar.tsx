import React, {FC, useEffect, useRef} from "react";
import { Calendar as CalendarDHX } from "dhx-suite";
import "dhx-suite/codebase/suite.min.css";
import {Box} from "@chakra-ui/react";

interface CalendarProps {
	value?: string | Date;
	date?: string | Date;
	css?: string;
	mark?: () => void;
	disabledDates?: () => void;
	weekStart?: "monday" | "sunday";
	weekNumbers?: boolean;
	mode?: "calendar" | "year" | "month" | "timepicker";
	timePicker?: boolean;
	dateFormat?: string;
	timeFormat?: 24 | 12;
	thisMonthOnly?: boolean;
	width?: string;
	range?: boolean;
	onReady: (item: CalendarDHX) => void;
}

const Calendar: FC<CalendarProps> = (props) => {
	const elRef = useRef<HTMLDivElement>(null);
	let calendar: CalendarDHX;

	useEffect(() => {
		if (elRef.current) {
			const newCalendar = new CalendarDHX(elRef.current, {
				css: "dhx_widget--bordered",
				value: new Date(),
			});

			if (calendar) {
				calendar.destructor();
			}
			calendar = newCalendar;
			if (props.onReady) {
				props.onReady(newCalendar);
			}
		}
	}, [props.css]);

	return <Box ref={elRef}></Box>;
};

export default Calendar;
