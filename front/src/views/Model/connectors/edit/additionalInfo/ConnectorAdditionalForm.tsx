import { Checkbox, Collapse, Flex, Input, Radio, Select, Spin, Table, Tooltip, Typography } from 'antd'
import React, { useEffect, useState } from 'react'
import { Connector, IModelStructComponents } from '../../../../../shared/entities/Connector/Connector'
import { useParams } from 'react-router-dom'
import { useAppSelector } from '../../../../../redux/Store'
import type { GetProp, RadioChangeEvent } from 'antd';
import { ModelsController } from '../../../../../controllers/ModelsController'
import { useDispatch } from 'react-redux'
import { useFormikContext } from 'formik'
import { CheckboxValueType } from 'antd/es/checkbox/Group'

function ConnectorAdditionalForm({ defaultInfo, connectorType }: { defaultInfo?: Connector, connectorType: 'input' | 'output' }) {

	const dispatch = useDispatch()
	const formik = useFormikContext()
	const { fromModel } = useParams()
	const modelComponents = useAppSelector(store => store.ModelReducer.model?.modelStructComponents)
	const [additionalInformation, setAdditionalInformation] = useState<IModelStructComponents>()
	const [checkedValuesList, setCheckedValues] = useState<any>()
	const modelsController = new ModelsController(dispatch);

	// const { dataFormat, daysRemaining, inputDataType } = defaultInfo as Connector
	const { dataFormat = '', daysRemaining = '', inputDataType = '' } = defaultInfo || {};

	const [defaultDataFormat, setDefaultDataFormat] = useState(dataFormat)
	const [editedInfoId, setEditedInfoId] = useState<number>()
	const [defaultDaysRemaining, setDefaultDaysRemaining] = useState(defaultInfo && defaultInfo.daysRemaining ? defaultInfo.daysRemaining : '')
	const [defaultInputDataType, setDefaultInputDataType] = useState('')

	const [value, setValue] = useState('GET');
	const [checkedList, setCheckedList] = useState<CheckboxValueType[]>();

	const onMethodChange = (e: RadioChangeEvent) => {
		console.log('radio checked', e.target.value);
		setValue(e.target.value);
	};

	const onOutputCheckboxChange = (list: CheckboxValueType[]) => {
		setCheckedList(list);
	};

	useEffect(() => {
		if (fromModel) {
			modelsController.getModel(fromModel as string).then(res => { })
		}
	}, [])

	useEffect(() => {
		if (modelComponents) {
			setAdditionalInformation(modelComponents.find((component: IModelStructComponents) => component.modelComponentId === defaultInfo?.versionId))
			setEditedInfoId(modelComponents.find((component: IModelStructComponents) => component.modelComponentId === defaultInfo?.versionId)?.modelComponentId)
		}
		if (defaultInfo) {
			setDefaultDataFormat(dataFormat)
			setDefaultInputDataType(inputDataType)
		}

	}, [modelComponents, defaultInfo?.versionId, defaultInfo, dataFormat, inputDataType])


	const onChangeCheckbox: GetProp<typeof Checkbox.Group, 'onChange'> = (checkedValues) => {
		setCheckedValues(checkedValues);
	};

	const setDataFormat = (e: RadioChangeEvent) => {
		formik.setFieldValue('dataFormat', e.target.value)
		setDefaultDataFormat(e.target.value)
	};

	const selectInputDataType = (value: string) => {
		formik.setFieldValue('inputDataType', value)
		setDefaultInputDataType(value)
	}

	const checkboxoOptions = [
		{ label: 'Проверять на соответствие основным атрибутам', value: 0 },
		{
			label: checkedValuesList && checkedValuesList.some((item: any) => item === 1) ?
				<div style={{ width: '100%' }}>
					<span> Сохранять полученные данные</span>
					<Input
						defaultValue={additionalInformation?.daysRemaining}
						variant="borderless"
						style={{ width: 50, borderBottom: '1px solid #d9d9d9', padding: 0, paddingLeft: 10 }}
					/>
					<span>дней</span>
					<Select
						value={additionalInformation?.resultIncremental}
						onClick={(e) => e.preventDefault()}
						style={{ width: 160, height: 25, marginLeft: 5 }}
						options={[
							{ value: false, label: 'Заменять новыми данными' },
							{ value: true, label: 'Инкрементально' },
						]}
					/>
				</div> : `Сохранять полученные данные ${additionalInformation?.daysRemaining} дней ${additionalInformation?.resultIncremental === true ? 'Инкрементально' : 'и заменять новыми данными'}`,
			value: 1
		}
		,
	];

	const outputConFromModelOptions = [
		{ label: 'Отправлять по мере обработки правилами', value: 0 },
		{ label: 'Отправлять по расписанию', value: 1 },
		{
			label: checkedValuesList && checkedValuesList.some((item: any) => item === 1) ?
				<div style={{ width: '100%' }}>
					<span> Сохранять полученные данные</span>
					<Input
						defaultValue={additionalInformation?.daysRemaining}
						variant="borderless"
						style={{ width: 50, borderBottom: '1px solid #d9d9d9', padding: 0, paddingLeft: 10 }}
					/>
					<span>дней</span>
					<Select
						value={additionalInformation?.resultIncremental}
						onClick={(e) => e.preventDefault()}
						style={{ width: 160, height: 25, marginLeft: 5 }}
						options={[
							{ value: false, label: 'Заменять новыми данными' },
							{ value: true, label: 'Инкрементально' },
						]}
					/>
				</div> : `Сохранять полученные данные ${additionalInformation?.daysRemaining} дней ${additionalInformation?.resultIncremental === true ? 'Инкрементально' : 'и заменять новыми данными'}`, value: 2,
		},
	];


	return (
		<Flex style={{ marginTop: 20 }} gap='middle' vertical>

			<Collapse
				size="small"
				ghost
				items={[{
					key: '1',
					label: <Typography.Title level={5} style={{ width: '25%' }}>Прочие настройки</Typography.Title>,
					children: <Flex style={{ width: '100%' }} justify='flex-start' gap='middle' vertical>

						{/* <Flex style={fromModel ? { width: '25%' } : { width: '100%' }} vertical={fromModel ? true : false} gap='middle'> */}

						<Flex style={{ width: '100%' }} vertical gap='middle'>

							{connectorType === 'input' &&
								<Flex gap='middle' style={{ width: '100%' }}>

									<Tooltip title="Вид получаемых данных">
										<span style={{ height: 40, width: '25%' }} >Вид данных</span>
									</Tooltip>

									<Select
										value={defaultInputDataType}
										style={{ width: '40%' }}
										options={[
											{ value: 'STRUCTURED_DATA', label: 'Структурированные текстовые данные' },
											{ value: 'UNSTRUCTURED_DATA', label: 'Неструктурированные текстовые данные' },
											{ value: 'VIDEO_STREAM', label: 'Видео' },
											{ value: 'АUDIO_STREAM', label: 'Аудио' },
										]}
										onChange={selectInputDataType}
									/>
								</Flex>
							}


							{connectorType === 'output' && <Flex gap='middle'>
								<span style={{ width: '25%' }}>Метод</span>
								<Radio.Group style={{ width: '75%' }} value={value} onChange={onMethodChange}>
									<Radio value={'GET'}>
										GET
									</Radio>
									<Radio value={'POST'}>
										POST
									</Radio>
								</Radio.Group>
							</Flex>}


							{value === 'POST' && <Flex gap='middle' style={{ width: '100%' }}>
								<Tooltip title="Формат получаемых данных на обработку">
									<span style={{ height: 40, width: '25%' }}>Формат данных</span>
								</Tooltip>
								<Radio.Group
									value={defaultDataFormat}
									onChange={setDataFormat}
								>
									<Flex style={{ width: '40%' }}>
										<Radio value={'JSON'}>JSON</Radio>
										<Radio value={'TEXT'}>txt</Radio>
										<Radio value={'HTML'}>HTML</Radio>
										<Radio value={'XML'}>XML</Radio>
										<Radio value={'ETC'}>etc</Radio>
									</Flex>
								</Radio.Group>
							</Flex>}

							<Flex gap='middle' style={{ width: '100%' }}>

								<span style={{ height: 40, width: '25%' }}>Протокол</span>

								<Radio.Group
									value={defaultDataFormat}
									onChange={setDataFormat}
								>
									<Flex style={{ width: '100%' }}>
										<Radio value={'HTTP'}>HTTP-REST</Radio>
										<Radio value={'SYSTEM'}>FILE SYSTEM</Radio>
										<Radio value={'ORACLE'}>Oracle</Radio>
										<Radio value={'ETC'}>etc</Radio>
									</Flex>
								</Radio.Group>
							</Flex>

						</Flex>

						{fromModel && connectorType === 'input' &&
							<Flex gap='middle' style={{ width: '100%' }} vertical>

								<Flex gap='small' style={{ width: '100%' }}>
									<span style={{ height: 40, width: '25%' }}>
										Макс. количество запросов в секунду
									</span>

									<Input style={{ width: 60, height: 25, marginLeft: 5 }} />
								</Flex>

								<Flex gap='small' style={{ width: '100%' }}>
									<span style={{ height: 40, width: '25%' }}>Получение</span>
									<Checkbox.Group
										options={checkboxoOptions}
										onChange={onChangeCheckbox}
										style={{ width: '60%' }}
									/>
								</Flex>

								<Flex style={{ width: '100%' }} gap='small' >
									<span style={{ height: 40, width: '25%' }}>Ответ на запрос</span>

									<Radio.Group style={{ width: '75%' }} value={1}>
										<Radio value={1}>
											Сразу после получения только ответить об успешном получении, не дожидаясь передачи в правила
										</Radio>
										<Radio style={{ marginTop: 10 }} value={2}>
											Ответить полезной нагрузкой после полной обработки правилами этим же коннектором в рамках той же сессии
										</Radio >
									</Radio.Group>
								</Flex>
							</Flex>
						}



						{(connectorType === 'output' && fromModel) && <Flex gap='small'>
							<span style={{ width: '25%' }}>Ответ на запрос</span>
							<Checkbox.Group
								style={{ display: 'flex', flexDirection: 'column' }}
								options={outputConFromModelOptions}
								onChange={onOutputCheckboxChange}
							/>
						</Flex>

						}



					</Flex>
				}]}
			/>

			{/* onOutputCheckboxChange */}
			{connectorType === 'output' && checkedList?.includes(1) && <Flex gap='middle' style={{ marginTop: 20 }}>
				<Typography.Title level={5} style={{ width: '25%' }}>Расписание</Typography.Title>
				<Table style={{ width: '75%', border: '1px solid black' }} showHeader={false} />
			</Flex>}
		</Flex >
	)


}

export default ConnectorAdditionalForm