import { useEffect, useState } from 'react'
import { Space, Typography, message, Flex } from 'antd'
import { RulesController } from '../../../../controllers/RulesController';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { useAppSelector } from '../../../../redux/Store';
import { Formik } from 'formik';
import { FieldVariable, QualityRule, Rule, RuleRequest, RuleType } from '../../../../shared/entities/Rule/Rule';
import AntFormRule from './AntForm';
import RulesDataStructureTable from './dataStructureTable';
import { GridRuleOperation } from './GridRuleOperation';
import ConditionFields from './conditionFields';
import { ActionTypes } from '../../../../redux/Model/RuleReducer';
import ReturnButton from '../../../../components/ReturnButton/ReturnButton';
import CurrentResultOfTestData from './testData/CurrentResultOfTestData';

interface JsonDataType {
	number?: number
	condition: string
	resultCondition?: string
}

function RuleDefaultForm({ edit }: { edit: boolean }) {
	const dispatch = useDispatch();
	const rulesController = new RulesController(dispatch);
	const { id, fromModel } = useParams();
	const { Title } = Typography;

	const [messageApi, contextHolder] = message.useMessage();
	const { rule, selectedJsonData, defaultFieldList, resultOfTestData } = useAppSelector(store => store.RuleReducer)
	const [ruleData, setRuleData] = useState<Rule | undefined>(undefined);
	const [jsonDataValue, setJsonDataValue] = useState<JsonDataType>()



	useEffect(() => {
		if (id !== null && id !== undefined) {
			rulesController.get(id).then(res => {
			})
			setRuleData(rule || {} as Rule);
			if (fromModel) {
				rulesController.accessField(fromModel, id).then(res => {
					rulesController.getFieldRelation(Number(fromModel))
				})
			}
		}
	}, [id]);


	const setJSONData = (data: any) => {

		setJsonDataValue(data);

		if (rule?.summarySubType === 'QUANTITY') {
			console.log('@QUANTITY', selectedJsonData)
			dispatch({
				type: ActionTypes.RULE_EDIT_JSON_DATA,
				payload: {
					// ...selectedJsonData,
					isQuality: false,
					newCondition: data?.condition,
					newResultCondition: data?.resultCondition,
				}
			});
		} else {

			dispatch({
				type: ActionTypes.RULE_EDIT_JSON_DATA,
				payload: {
					...selectedJsonData,
					// textValue: selectedJsonData?.textValue,
					// toIncidents: selectedJsonData?.toIncidents,
					isQuality: true,
					number: selectedJsonData && selectedJsonData.number,
					newCondition: data?.condition
				}
			});
		}
	}

	useEffect(() => {

		if (rule && rule.jsonData) {

			dispatch({
				type: ActionTypes.RULE_SELECT_JSON_DATA,
				payload: rule.jsonData ? rule.jsonData[0] : {
					condition: '',
					resultCondition: ''
				}
			})
		}

	}, [rule])


	return (

		<Formik
			initialValues={ruleData || {
				name: "",
				description: "",
				saveResult: false,
				ruleType: RuleType.QUANTITY,
				jsonData: [],
				fields: [],
				extended: null,
			}}

			onSubmit={(values: any, actions) => {
				values.isCreate = !edit
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
						summarySubType: rule ? rule.summarySubType : '',
						isTemplate: false,
						name: values.name,
						description: values.description,
						saveResult: true,
						versionId: values.versionId || null,
						ruleType: rule?.ruleType as string,
						queueNumber: 1,
						extended: values.extended,
						resultIncremental: false,
						jsonData: rule?.jsonData && rule.jsonData.length > 0 ?
							rule?.jsonData.map(item => {
								if (item.hasOwnProperty('sendingId')) {
									const { sendingId, ...rest } = item as any
									return { ...rest, id: -1 };
								}
								return item;
							})
							: [],
						fields: values.extended ? [] :
							rule?.fields.map(field => ({
								name: field.name,
								testValueJson: field.testValueJson,
								id: defaultFieldList.includes(field.id) ? field.id : -1
							})) as FieldVariable[]
					};
					rulesController.createOrUpdate(ruleRequest)
						.then(res => {
							if (!res?.None) {
								const successMessage = "Rule " + res.Some + " successfully" + edit ? "updated!" : "created!";
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
						}).then(() => { rulesController.get(String(id)) })
				}
				actions.setSubmitting(false)
			}}
		>
			<Space direction="vertical" size="middle" style={{ padding: 20, width: "100%" }}>
				{contextHolder}
				<Title level={2}>{edit ? 'МОДИФИКАЦИЯ ПРАВИЛА' : 'СОЗДАНИЕ ПРАВИЛА'}</Title>

				<ReturnButton tab='3' fromModel={fromModel && fromModel} />

				<AntFormRule data={rule || {}} edit={edit} />

				<Flex style={{ width: '100%' }} gap='middle'>
					<span style={{ width: '25%' }}>Входящая структура данных</span>
					<RulesDataStructureTable data={rule?.fields || []} />
				</Flex>



				{rule?.summarySubType === RuleType.QUALITY &&
					<>
						<Flex style={{ width: '100%' }} gap='middle'>
							<span style={{ width: '25%', paddingTop: '47px' }}>Уровни или индексы</span>
							<GridRuleOperation />
						</Flex>
					</>

				}


				<CurrentResultOfTestData />




				<ConditionFields
					data={selectedJsonData ? selectedJsonData : []}
					isQuality={rule?.summarySubType === RuleType.QUALITY}
					setJSONData={setJSONData}
				/>

			</Space>
		</Formik>

	)
}

export default RuleDefaultForm