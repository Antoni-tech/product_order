import { Button, Flex, Spin, message } from 'antd'
import React, { useEffect } from 'react'
import { IResultOfTestData, RuleType } from '../../../../../shared/entities/Rule/Rule'
import { useAppSelector } from '../../../../../redux/Store'
import { useDispatch } from 'react-redux'
import { RulesController } from '../../../../../controllers/RulesController'
import { useParams } from 'react-router-dom'
import { ActionTypes } from '../../../../../redux/Model/RuleReducer'

function CurrentResultOfTestData() {
	const dispatch = useDispatch()

	const { rule, resultOfTestData } = useAppSelector(store => store.RuleReducer)
	const rulesController = new RulesController(dispatch)
	const [messageApi, contextHolder] = message.useMessage();


	useEffect(() => {
		if (rule)
			dispatch({
				type: ActionTypes.RULE_GET_RESULT_OF_TESTDATA,
				payload: { result: 'undefined' }
			})
	}, [])

	const getResult = () => {

		const testdata: { [key: string]: string | number } = rule?.fields
			? rule.fields.reduce((acc, field) => {
				if (field.testValueJson !== null) {
					acc[field.name] = field.testValueJson;
				}
				return acc;
			}, {} as { [key: string]: string | number })
			: {};

		if (rule && rule.summarySubType === 'QUALITY') {

			const conditions: { number: number; condition: string }[] = rule?.jsonData
				? rule.jsonData.map((item) => ({
					//@ts-ignore
					number: item.number as number,
					condition: item.condition
				}))
				: [];

			rulesController.qualityRuleEvaluate('quality', testdata, conditions)

		}
		else {

			const quantityCondition = rule && rule.jsonData && rule?.jsonData[0].condition
			//@ts-ignore
			const quantityResCondition = rule && rule.jsonData && rule?.jsonData[0].resultCondition


			rulesController.quantityRuleEvaluate('quantity', testdata, quantityCondition as string, quantityResCondition)
				.catch(error => {
					const errorMessage = error?.response?.data?.error || 'Unknown error occurred';
					messageApi.open({
						type: 'error',
						content: `Error: ${errorMessage}`,
					});
				});


		}

	}


	if (rule)
		return (

			<Flex style={{ width: '100%' }} gap='middle'>

				{contextHolder}

				<span style={{ width: '25%' }}>Текущее тестовое значение</span>

				<Flex gap='middle' justify='flex-start' align='center'>

					<Button onClick={getResult}>Calculate</Button>

					{rule?.summarySubType === RuleType.QUALITY && rule?.jsonData && Array.isArray(resultOfTestData) ? (
						<span style={{ width: '70%' }}>
							{resultOfTestData.filter(item => item.result === "true")[0]?.number + '-' +
								(rule.jsonData.find(item => item.number === resultOfTestData.filter(item => item.result === "true")[0]?.number))?.textValue}
						</span>
					) : (
						<span>
							Result: {resultOfTestData && 'result' in resultOfTestData
								? String((resultOfTestData as IResultOfTestData).result)
								: ''
							}

						</span>
					)}


				</Flex>


			</Flex>
		)

	else return <Spin />

}

export default CurrentResultOfTestData