import React, { useEffect, useState } from 'react'
import { Flex, Typography, Input, Spin, Select, Collapse } from 'antd'
import { useAppSelector } from '../../../../redux/Store'
import { useParams } from 'react-router-dom'
import { ModelsController } from '../../../../controllers/ModelsController'
import { useDispatch } from 'react-redux'
import { useFormikContext } from 'formik'
import { ActionTypes } from '../../../../redux/Model/ModelReducer'
import type { SelectProps } from 'antd';
import type { CollapseProps } from 'antd';

const SingleField = ({ title, children }: { title: string, children: React.ReactNode }) => {
	return (
		<Flex style={{ width: '100%', paddingLeft: 20 }} gap='middle' justify='flex-start'>
			<Typography.Title level={5} style={{ width: '25%' }}>{title}</Typography.Title>
			{children}
		</Flex>
	)
}



function ModelInfo() {

	const model = useAppSelector(store => store.ModelReducer.model);
	const { id } = useParams();
	const dispatch = useDispatch()
	const modelsController = new ModelsController(dispatch);

	const formik = useFormikContext()

	const [nameValue, setNameValue] = useState('')
	const [typeValue, setTypeValue] = useState('')
	const [descriptionValue, setDescriptionValue] = useState('')

	const options: SelectProps['options'] = [
		{
			label: 'Управление стратегией',
			value: 0
		},
		{
			label: 'Управление и разработка продуктов и услуг',
			value: 1
		},
		{
			label: 'Управление маркетингом и продажами',
			value: 2
		},
		{
			label: 'Управление поставками',
			value: 3
		},
		{
			label: 'Производство',
			value: 4
		},
		{
			label: 'Предоставление продуктов и услуг',
			value: 5
		},
		{
			label: 'Управление клиентским сервисом',
			value: 6
		},
		{
			label: 'Управление финансами',
			value: 7
		},
		{
			label: 'Управление персоналом',
			value: 8
		},
	];

	// useEffect(() => {
	// 	if (id !== null && id !== undefined) {
	// 		modelsController.getModel(id).then(() => {
	// 		})
	// 	}
	// }, [id, modelsController]);

	useEffect(() => {
		if (model) {
			setNameValue(model.name || '');
			setTypeValue(model.type || '');
			setDescriptionValue(model.description || '');
		}
	}, [model]);


	const onChangeField = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, type: string) => {
		const { value } = e.target;
		if (type === 'name') {
			setNameValue(value);
			dispatch({
				type: ActionTypes.MODEL_EDIT_MODEL_INFO,
				payload: { name: value, type: typeValue, description: descriptionValue }
			})
		}
		if (type === 'type') {
			setTypeValue(value);
			dispatch({
				type: ActionTypes.MODEL_EDIT_MODEL_INFO,
				payload: { name: nameValue, type: value, description: descriptionValue }
			})
		}
		if (type === 'description') {
			setDescriptionValue(value);
			dispatch({
				type: ActionTypes.MODEL_EDIT_MODEL_INFO,
				payload: { name: nameValue, type: typeValue, description: value }
			})
		}
	}
	const handleChange = (value: number[]) => {
		console.log(`selected ${value}`);
	};

	const items: CollapseProps['items'] = [
		{
			key: '1',
			label: <Typography.Title level={5} style={{ color: '#BDBDBD' }}>Дополнительная информация</Typography.Title>,
			children: <Flex vertical gap='middle'>
				<SingleField title='Краткое описание'>
					<Input.TextArea
						value={descriptionValue}
						variant="filled"
						rows={4}
						onChange={(e) => onChangeField(e, 'description')}
						style={{ width: '40%', resize: 'none' }}
					/>
				</SingleField>

				<SingleField title='Процесс - источник'>
					<Select
						mode="multiple"
						maxTagCount={'responsive'}
						allowClear
						style={{ width: '40%', height: 30 }}
						placeholder="Please select"
						onChange={handleChange}
						options={options}
					/>
				</SingleField>


				<SingleField title='Процесс - назначение'>
					<Select
						mode="multiple"
						allowClear
						maxTagCount={'responsive'}
						style={{ width: '40%', height: 30 }}
						placeholder="Please select"
						onChange={handleChange}
						options={options}
					/>
				</SingleField>

				<SingleField title='Макс. время на все этапы'>
					<Input style={{ width: 130, height: 30 }} addonAfter="ms" />
				</SingleField>
			</Flex>,
		},
	]



	if (model) {
		// const { type, name, description } = model;
		return (
			<Flex vertical gap='middle' style={{ marginBottom: 50 }}>

				<Flex vertical gap='middle' >
					<SingleField title='Идентификатор'>
						<span>{model.versionId}</span>
					</SingleField>

					<SingleField title='Создал'>
						<span>{model.userName}</span>
					</SingleField>

					<SingleField title='Наименование'>
						<Input
							value={nameValue}
							onChange={(e) => onChangeField(e, 'name')}
							variant="filled"
							style={{ width: '40%', height: 30 }}

						/>
					</SingleField>
				</Flex>

				<Collapse ghost items={items} style={{ paddingLeft: 0 }} />

			</Flex>
		)


	}

	return (
		<Spin />
	)
}

export default ModelInfo