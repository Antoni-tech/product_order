import { Form, Formik, useFormikContext } from "formik";
import React, { FC, useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { Box, Heading, Textarea, Text } from "@chakra-ui/react";
import { BoxRowStyleConfig, BoxStyleConfig } from "../../../Forms/FormStyleConfigs";
import { Card } from "../../../../components/Card/Card";
import DHXFormRule from "./DHXFormRule";
import { ModalService } from "../../../../service/Modal/ModalService";
import { useParams } from "react-router";
import { RulesController } from "../../../../controllers/RulesController";
import { MainButton } from "../../../../components/Buttons/MainButton";
import { GridRuleOperation } from "./GridRuleOperation";
import { GridRuleVariable } from "./GridRuleVariable";
import { message, Input } from 'antd'
import { CheckOutlined } from '@ant-design/icons'

import { QualityRule, QuantityRule, RuleRequest, RuleType } from "../../../../shared/entities/Rule/Rule";
import { useAppSelector } from "../../../../redux/Store";
import { ComponentType } from "../../../../common/constants";
import RulesDataStructureTable from "./dataStructureTable";
import AntFormRule from "./AntForm";
import { ActionTypes } from "../../../../redux/Model/RuleReducer";


export const FormRule: FC<{ edit: boolean }> = (edit) => {
	const dispatch = useDispatch();
	const rulesController = new RulesController(dispatch);
	const ConnectorFormCardConfig = { position: "relative" }
	const modalService = new ModalService(dispatch);
	const { id, fromModel } = useParams();

	// const [condition, setCondition] = useState<string>("");
	const [ruleType, setRuleType] = useState<RuleType>(RuleType.QUANTITY);
	const [selCondition, setSelCondition] = useState<number | null>(null);

	// const [fromModelValue, setFromModelValue] = useState(fromModel);
	const { rule, selectedJsonData } = useAppSelector(store => store.RuleReducer)
	const event = useAppSelector(store => store.DHTMLXReducer.event)
	const [messageApi, contextHolder] = message.useMessage();

	const [condition, setCondition] = useState<null | string>(Array.isArray(rule?.jsonData) ? selectedJsonData &&
		selectedJsonData.condition ? selectedJsonData.condition :
		(rule && rule?.jsonData ? rule.jsonData[0].condition : '') : ''
	)

	const [resultCondition, setResultCondition] = useState(() => {

		if (selectedJsonData && selectedJsonData.resultCondition) {
			return selectedJsonData.resultCondition;
		} else if (rule && rule.jsonData && rule.jsonData[0]) {
			const jsonDataItem = rule.jsonData[0];
			if ('resultCondition' in jsonDataItem) {
				return jsonDataItem.resultCondition;
			}
		}
		return '';

	});

	useEffect(() => {
		if (id !== null && id !== undefined) {
			rulesController.get(id).then(res => {
			})
		}
		console.log('@fromModel', fromModel, id)
	}, [id])

	useEffect(() => {
		if (rule) {
			setRuleType(rule.ruleType)

			switch (rule.ruleType) {
				// case RuleType.QUALITY:
				// 	// let res: Array<QualityRule> = JSON.parse(rule?.jsonData)
				// 	let res: Array<QualityRule | QuantityRule> = rule?.jsonData
				// 	if (res.length > 0) {
				// 		setCondition(res[0].condition);
				// 		// form.getItem("resultCondition")?.hide()
				// 	}
				// 	break;

				case RuleType.QUALITY:
					let res: Array<QualityRule | QuantityRule> = rule?.jsonData || [];

					if (Array.isArray(res) && res.length > 0) {
						const qualityRule = res[0] as QualityRule; // Приведем к типу QualityRule
						// setCondition(qualityRule.condition);
						// form.getItem("resultCondition")?.hide()
						setCondition(selectedJsonData && selectedJsonData.condition)

					}
					break;

				case RuleType.QUANTITY:
					// // form.getItem("resultCondition").show()
					// // JSON.parse(rule?.jsonData).forEach((value: QuantityRule, index: number) => {
					// 	rule?.jsonData.forEach((value: QuantityRule , index: number) => {
					// 	setCondition(value.condition);
					// 	setResultCondition(value.resultCondition);
					// })
					if (Array.isArray(rule?.jsonData)) {
						rule.jsonData.forEach((value: QuantityRule | QualityRule, index: number) => {
							if ('resultCondition' in value) {
								setCondition(value.condition);
								setResultCondition(value.resultCondition);
							}
						});
					}


					break;
			}

		}

		else {
			dispatch({
				type: ActionTypes.RULE_GET,
				payload: {
					isCreate: false,
					versionId: "2199",
					type: "CONNECTOR_INPUT",
					name: "new Rule",
					description: "new rule description",
					validateFields: true,
					saveResult: true,
					ruleType: "QUANTITY",
					queueNumber: 0,
					daysRemaining: 0,
					resultIncremental: true,
					jsonData: {
						condition: '',
						resultCondition: ''
					},
					fields: [

					]
				}
			})
			dispatch({
				type: ActionTypes.RULE_SELECT_JSON_DATA,
				payload: {
					condition: "(current?.sumPay > 500) AND (prev?.accCt == 'KZ32115B12')",
					resultCondition: "(current?.sumPay + 500)"
				}
			})
		}
	}, [rule, selectedJsonData]);

	const handleConditionChange = (e: any) => {
		let inputValue = e.target.value
		setCondition(inputValue)
	}

	const handleResultConditionChange = (e: any) => {
		let inputValue = e.target.value
		setResultCondition(inputValue)
	}

	useEffect(() => {
		if (event !== null && event.componentType === ComponentType.FormRules) {
			// setCondition(event.object)
			setSelCondition(Number(event.id))
		}
	}, [event]);




	return (

		<Card {...ConnectorFormCardConfig} >
			{contextHolder}
			<Heading as='h4' size='md' ml={180} mb={"30px"}>
				{edit.edit ? 'МОДИФИКАЦИЯ ПРАВИЛА' : 'СОЗДАНИЕ ПРАВИЛА'}</Heading>
			<Formik initialValues={{}}
				validate={values => {
					console.log("values:", values)
				}}
				onSubmit={(values: any, actions) => {
					values.isCreate = !edit.edit
					values.isTemplate = false
					if (!values.isCreate) {
						values.versionId = id
					}

					if (values.fields === null) {
						messageApi.open({
							type: 'error',
							content: "Not variable rule",
						});

					} else {
						const ruleRequest: RuleRequest = {
							isCreate: values.isCreate,
							type: "RULE",
							isTemplate: false,
							name: values.name,
							summarySubType: rule ? rule?.summarySubType : '',
							description: values.description,
							saveResult: true,
							versionId: values.versionId || "",
							extended: null,
							// ruleType: values.ruleType,
							ruleType: rule?.ruleType as string,
							queueNumber: 1,
							resultIncremental: false,
							// jsonData: values.fields_operation,
							jsonData: rule?.ruleType === RuleType.QUALITY ? values?.jsonData as any : [{ condition, resultCondition }],
							fields: values.fields ? values.fields.map(({ id, ...rest }: { id: number }) => rest) : []
							// fields: values.fields
							// 	? values.fields.filter(({ id }: { id: number }) => defaultFieldList.includes(id))
							// 	: []
						};
						rulesController.createOrUpdate(ruleRequest)
							.then(res => {
								if (!res?.None) {
									const successMessage = "Rule " + res.Some + " successfully" + edit.edit ? "updated!" : "created!";
									messageApi.open({
										type: 'success',
										content: successMessage,
									});
								} else {
									messageApi.open({
										type: 'error',
										content: "Error create rule",
									});
								}
							})
					}
					actions.setSubmitting(false)
				}}
			>
				{({ isSubmitting, errors, touched, handleSubmit }) => (
					<Form onSubmit={handleSubmit} autoComplete="off" style={{ marginBottom: 150 }}>

						<Box {...BoxStyleConfig}>
							{/* <DHXFormRule resultCondition={resultCondition} condition={condition} /> */}
							{/* 
							<AntFormRule
								resultCondition={resultCondition}
								condition={ }
								data={rule || {}}
								edit={edit.edit}
							/> */}


							{/* <RulesDataStructureTable /> */}


							{/* <Box ml={"8px"}>
								<GridRuleVariable />
							</Box> */}

							{ruleType === RuleType.QUALITY &&
								<Box ml={"8px"} minW={"600px"}>
									<GridRuleOperation />
								</Box>
							}

						</Box>
						<Box {...BoxRowStyleConfig} width={ruleType === RuleType.QUANTITY ? "100%" : "50%"}>
							{ruleType === RuleType.QUANTITY && (
								<Box width={"100%"} padding={1}>
									<Input.Search
										addonBefore="РАВНО:"
										defaultValue={resultCondition}
										onChange={handleResultConditionChange}
										enterButton={<CheckOutlined />}
										onSearch={() => dispatch({
											type: ActionTypes.RULE_EDIT_JSON_DATA,
											payload: {
												number: selectedJsonData && selectedJsonData.number,
												newConnewResultConditiondition: resultCondition
											}
										})}
									/>
								</Box>
							)}
							<Box width={"100%"} padding={1}>
								<Input.Search addonBefore="ЕСЛИ:"
									value={condition as string}
									onChange={handleConditionChange}
									onFocus={() => setCondition(condition)}
									onSearch={() =>
										dispatch({
											type: ActionTypes.RULE_EDIT_JSON_DATA,
											payload: {
												number: selectedJsonData && selectedJsonData.number,
												newCondition: condition
											}
										})
									}
									enterButton={<CheckOutlined />}
								/>
							</Box>
						</Box>

					</Form>
				)}
			</Formik>
		</Card>
	);
}
