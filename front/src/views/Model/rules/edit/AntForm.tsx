import React, { useEffect, useMemo, useRef, useState } from 'react'
import { Flex, Switch, Radio, Input, Select } from 'antd'
import { useFormikContext } from 'formik';
import { useAppSelector } from '../../../../redux/Store';
import type { RadioChangeEvent } from 'antd';
import { useParams } from 'react-router-dom';
import { RulesController } from '../../../../controllers/RulesController';
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../../../redux/Model/RuleReducer';
import { CheckOutlined, CloseOutlined } from '@ant-design/icons';


interface IAntFormRule {
	// resultCondition?: string;
	// condition?: string;

	data: any
	edit: boolean
}

type IFormikForm = {
	name: string
}

type ISingleFormElement = { title: string, children: React.ReactNode }

const SingleFormElement: React.FC<ISingleFormElement> = ({ title, children }) => {
	return (
		<Flex style={{ width: '100%' }} gap='middle'>
			<span style={{ width: '25%' }}>{title}</span>
			{children}
		</Flex>
	)
}


const AntFormRule: React.FC<IAntFormRule> = ({ data, edit }) => {

	const { rule } = useAppSelector(store => store.RuleReducer)

	const { name, description, summarySubType } = data

	const dispatch = useDispatch();
	const formik = useFormikContext<IFormikForm>()

	// const rulesController = new RulesController(dispatch);

	const [resultValue, setResultValue] = useState(rule?.saveResult || false);
	const [resultIncrementalValue, setIncrementalValue] = useState(rule?.resultIncremental || false);

	const { id, fromModel } = useParams();

	// const [inputValue, setInputValue] = useState(rule?.name || "");
	// const [descriptionValue, setDescriptionValue] = useState(rule?.description || "");

	const [inputValue, setInputValue] = useState<string>();
	const [descriptionValue, setDescriptionValue] = useState(description);

	useEffect(() => {
		if (data) {
			setInputValue(name);
			setDescriptionValue(description);

			formik.setFieldValue("name", name);
			formik.setFieldValue("description", description);
		}
	}, [data, name, description]);



	// const [selCondition, setSelCondition] = useState<number>(0);
	const onChangeSaveResult = (e: RadioChangeEvent) => {
		// setValue(e.target.value);
		setResultValue(e.target.value)
		formik.setFieldValue("saveResult", e.target.value)
	};

	const resultIncremental = (e: RadioChangeEvent) => {
		// setValue(e.target.value);
		setIncrementalValue(e.target.value)
		formik.setFieldValue("resultIncremental", e.target.value)
	};

	const onSwitch = (checked: boolean) => {
		formik.setFieldValue("allowMultipleModels", checked)
	};

	const onSelect = (value: string) => {
		dispatch({
			type: ActionTypes.RULE_CHANGE_RULETYPE,
			payload: value
		})
	}

	const onChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
		// setInputValue(e.target.value)
		const newValue = e.target.value;
		setInputValue(newValue);
		dispatch({
			type: ActionTypes.RULE_EDIT,
			payload: {
				key: 'name',
				value: newValue
			}
		})

		formik.setFieldValue("name", newValue);
	}

	const onTextAreaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		setDescriptionValue(e.target.value)

		dispatch({
			type: ActionTypes.RULE_EDIT,
			payload: {
				key: 'description',
				value: e.target.value
			}
		})

		formik.setFieldValue("description", descriptionValue);
	}


	return (

		<Flex vertical gap='middle' style={{ marginBottom: '50px' }}>


			<SingleFormElement title='Идентификатор'>
				<span style={{ width: '70%' }}>{id ? id : null}</span>
			</SingleFormElement>

			<SingleFormElement title='Разрешить исп. в 2-х и более моделях'>
				<Switch
					onChange={onSwitch}
					size='default'
					checkedChildren={<CheckOutlined />}
					unCheckedChildren={<CloseOutlined />}
					style={{ backgroundColor: '#d9d9d9' }}
				/>
			</SingleFormElement>

			<SingleFormElement title='Тип документа'>
				<span>{summarySubType === 'QUALITY' ? 'Качество' : 'Количество'}</span>
			</SingleFormElement>

			<SingleFormElement title='Наименование'>
				<Input
					value={inputValue}
					placeholder={'Наименование правила'}
					onChange={onChangeInput}
					style={{ width: '70%' }}
				/>
			</SingleFormElement>

			<SingleFormElement title='Краткое описание'>

				<div style={{ width: '70%' }}>
					<Input.TextArea
						placeholder={'Краткое описание'}
						value={descriptionValue}
						onChange={onTextAreaChange}
					/>
				</div>
			</SingleFormElement>

			{(fromModel && edit === true) &&

				<SingleFormElement title='Сохранять результаты расчета'>


					<Flex style={{ width: '70%' }} justify='flex-start' gap='middle' align='center'>



						<Switch
							onChange={onSwitch}
							size='default'
							checkedChildren={<CheckOutlined />}
							unCheckedChildren={<CloseOutlined />}
							style={{ backgroundColor: '#d9d9d9' }}
						/>

						<Flex style={{ width: '15%' }} gap='small' align='center'>
							<Input style={{ height: 25 }} />
							<span>дней</span>
						</Flex>

						<Radio.Group onChange={resultIncremental} value={resultIncrementalValue} style={{ marginLeft: 30 }}>
							<Flex >
								<Radio value={true}>Инкрементально</Radio>
								<Radio value={false}>Заменять новыми данными</Radio>
							</Flex>
						</Radio.Group>
					</Flex>

				</SingleFormElement>
			}

		</Flex>
	)
}

export default AntFormRule