import React, { useEffect, useRef, useState } from "react";
import { Form as FormDHX } from "dhx-suite";

import { FormControl } from "@chakra-ui/react";
import { useFormikContext } from "formik";
import formConfig from "./Rules.json";
import { useParams } from "react-router";
import { useDispatch } from "react-redux";
import { useAppSelector } from "../../../../redux/Store";
import { RulesController } from "../../../../controllers/RulesController";
import { QualityRule, QuantityRule, RuleType } from "../../../../shared/entities/Rule/Rule";
import { ActionsType, ComponentType } from "../../../../common/constants";
import { DHTMLXTypes } from "../../../../redux/DHTMLX/DHTMLReducer";

interface FormProps {
	css?: string;
	width?: string | number;
	height?: string | number;
	rows?: any[];
	cols?: any[];
	title?: string;
	align?: "start" | "center" | "end" | "between" | "around" | "evenly";
	padding?: string | number;
	disabled?: boolean;
	resultCondition?: string;
	condition?: string;
}

const DHXFormRule: React.FC<FormProps> = (props) => {
	const [form, setForm] = useState<FormDHX>();
	const formInConnectorRef = useRef<HTMLDivElement | null>(null);
	const formik = useFormikContext();
	const { id } = useParams();
	const dispatch = useDispatch();
	const rulesController = new RulesController(dispatch);
	const rule = useAppSelector(store => store.RuleReducer.rule)
	const event = useAppSelector(store => store.DHTMLXReducer.event)
	const [selCondition, setSelCondition] = useState<number>(0);

	useEffect(() => {
		if (form && rule) {
			switch (rule.ruleType) {
				case RuleType.QUALITY:
					// let res: Array<QualityRule> = JSON.parse(rule?.jsonData)
					let res: Array<QualityRule | QuantityRule> = rule?.jsonData as Array<QualityRule | QuantityRule>;
					if (res.length > 0) {
						form?.setValue({ "condition": res[0].condition });
						// form.getItem("resultCondition")?.hide()
					}
					break;
				// case RuleType.QUANTITY:
				//     // form.getItem("resultCondition").show()
				//     JSON.parse(rule?.jsonData).forEach((value: QuantityRule, index: number) => {
				//         form?.setValue({"condition": value.condition});
				//         form?.setValue({"resultCondition": value.resultCondition});
				//     })

				//     break;
			}
			Object.entries(rule).forEach(([key, value]) => {
				form?.setValue({ [key]: value });
			});
		}
	}, [rule]);

	useEffect(() => {
		if (props.condition && formik.getFieldProps("fields_operation").value) {
			let updateList = formik.getFieldProps("fields_operation").value
				.map((value: any, index: number) => {
					if (index === selCondition) {
						return { ...value, condition: props.condition };
					} else {
						return value;
					}
				})
			formik.setFieldValue("fields_operation", updateList);
		}
	}, [props.condition, props.resultCondition]);

	useEffect(() => {
		if (event !== null && event.actions === ActionsType.SET && event.componentType === ComponentType.Rules) {
			form?.setValue({ "condition": event.object });
			setSelCondition(Number(event.id))
			// console.log("selCondition 12:", selCondition, Number(event.id))
		}
	}, [event]);

	useEffect(() => {
		if (formInConnectorRef.current && !form) {
			setForm(new FormDHX(formInConnectorRef.current, formConfig));
		}
		if (id !== null && id !== undefined && form) {
			rulesController.get(id).then(() => {
			})
		}
		if (form) {
			form?.forEach((item, index, array) => {
				if (item.config.name) {
					let curr = form.getItem(item.config.name);
					if (curr) {
						formik.setFieldValue(item.config.name, curr.getValue(), false);
					}
				}
			});
			form?.events.on("click", (id) => console.log(id, "click"));

			form?.events.on("change", (actions: any, value: any) => {
				if (value === RuleType.QUANTITY || value === RuleType.QUALITY) {
					dispatch({
						type: DHTMLXTypes.EVENT_TOOLTIP,
						payload: { actions: actions, componentType: ComponentType.FormRules, object: value }
					});
				}

				formik.setFieldValue(actions, value, false);
			}
			);
		}
	}, [id, form]);


	return <FormControl style={{ textAlign: "left", background: "#fff", width: "100%" }} ref={formInConnectorRef} />
}
	;

export default DHXFormRule;
