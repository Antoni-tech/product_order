import { Flex, Input, Affix, Typography, FloatButton } from 'antd'
import { useEffect, useState } from 'react'
import { SaveOutlined } from '@ant-design/icons'
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../../../redux/Model/RuleReducer';
import { useAppSelector } from '../../../../redux/Store';
import { useFormikContext } from "formik";
import { RulesController } from '../../../../controllers/RulesController';


function ConditionFields({ data, isQuality, setJSONData }: { data: any, isQuality: boolean, setJSONData: (data: any) => void }) {

	const [condition, setCondition] = useState<string>('');
	const [resultCondition, setResultCondition] = useState<string>('');
	const [editingCondition, setEditingCondition] = useState(false)

	const [bottom, setBottom] = useState<number>(0);


	const dispatch = useDispatch()
	const rulesController = new RulesController(dispatch);

	const formik = useFormikContext()
	const { isValueSelecting, valueToCondition, rule, selectedJsonData } = useAppSelector(store => store.RuleReducer)

	const SaveChanges = () => {
		if (isQuality === true) {
			setJSONData({ ...data, condition })
			formik.handleSubmit()
		}
		else {
			setJSONData({ ...data, condition, resultCondition })
			console.log('-', condition, '+', resultCondition)
			formik.handleSubmit()
		}
	}

	useEffect(() => {
		if (isQuality === true && data) {
			console.log('@data', data)
			setCondition(data.condition)
		}
		else if (isQuality === false && data) {
			console.log('@default', data)
			setCondition(data.condition)
			setResultCondition(data.resultCondition)
		}
	}, [isQuality, data]);

	useEffect(() => {
		if (isValueSelecting === true && editingCondition === true) {
			setCondition(condition + valueToCondition)
		}
		else if (isValueSelecting === true && editingCondition === false) {
			setResultCondition(resultCondition + valueToCondition)
		}
	}, [isValueSelecting])


	const handleConditionChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		let inputValue = e.target.value
		setCondition(inputValue)

		// dispatch({
		// 	type: ActionTypes.RULE_EDIT_JSON_DATA,
		// 	payload: {
		// 		// ...selectedJsonData,
		// 		isQuality: false,
		// 		newCondition: inputValue,
		// 		newResultCondition: resultCondition,
		// 	}
		// });

		if (rule?.summarySubType === 'QUANTITY') {
			dispatch({
				type: ActionTypes.RULE_EDIT_JSON_DATA,
				payload: {
					// ...selectedJsonData,
					isQuality: false,
					newCondition: inputValue,
					newResultCondition: resultCondition,
				}
			});
		}
		else {

			dispatch({
				type: ActionTypes.RULE_EDIT_JSON_DATA,
				payload: {
					...selectedJsonData,
					// textValue: selectedJsonData?.textValue,
					// toIncidents: selectedJsonData?.toIncidents,
					isQuality: true,
					number: selectedJsonData && selectedJsonData.number,
					newCondition: inputValue
				}
			});
		}

	}


	const handleResultConditionChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		let inputValue = e.target.value
		setResultCondition(inputValue)

		if (rule?.summarySubType === 'QUANTITY') {
			dispatch({
				type: ActionTypes.RULE_EDIT_JSON_DATA,
				payload: {
					// ...selectedJsonData,
					isQuality: false,
					newCondition: condition,
					newResultCondition: inputValue,
				}
			});
		}
	}

	return (
		<Affix offsetBottom={bottom}>

			<Flex gap='middle' style={{ backgroundColor: '#fff', zIndex: 5, height: 150 }}>
				<Flex vertical gap='small' style={{ width: '100%' }}>
					<Typography.Text>ЕСЛИ:</Typography.Text>
					<Input.TextArea
						disabled={selectedJsonData !== null && selectedJsonData.type === 'NO_EXPRESSION'}

						value={condition}
						onChange={handleConditionChange}
						onClick={() => {
							setEditingCondition(true)
							dispatch({
								type: ActionTypes.RULE_SELECT_VALUE_TO_CONDITION,
								payload: { isValueSelecting: false, valueToCondition: '' }
							})
						}

						}
						style={{ height: 90, resize: 'none' }}
					/>
				</Flex>

				{isQuality === false &&

					<Flex vertical gap='small' style={{ width: '100%' }}>
						<Typography.Text>РАВНО:</Typography.Text>
						<Input.TextArea
							value={resultCondition}
							onChange={handleResultConditionChange}
							style={{ height: 90, resize: 'none' }}

							onClick={() => {
								setEditingCondition(false)
								dispatch({
									type: ActionTypes.RULE_SELECT_VALUE_TO_CONDITION,
									payload: { isValueSelecting: false, valueToCondition: '' }
								})
							}}
						/>
					</Flex>

				}

				<FloatButton onClick={SaveChanges} icon={<SaveOutlined />} />

			</Flex>
		</Affix>
	)
}

export default ConditionFields