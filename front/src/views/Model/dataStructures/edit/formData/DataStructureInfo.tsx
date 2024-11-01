import { Flex, Input, Spin, Typography } from 'antd'
import React, { useEffect, useState } from 'react'
import { useAppSelector } from '../../../../../redux/Store'
import { SingleDataStructure } from '../../../../../shared/entities/DataStructure/DataStructure'
import { useDispatch } from 'react-redux'
import { ActionTypes } from '../../../../../redux/Model/DataStructureReducer'

const SingleField = ({ title, children }: { title: string, children: React.ReactNode }) => {
	return (
		<Flex style={{ width: '100%' }} gap='middle'>
			<Typography.Title level={5} style={{ width: '25%' }}>{title}</Typography.Title>
			{children}
		</Flex>
	)
}

function DataStructureInfo() {

	const dispatch = useDispatch()
	const dataStructure = useAppSelector(store => store.DataStructureReducer.dataStructure)
	const { name, versionId, description, models } = dataStructure ? dataStructure : {} as SingleDataStructure

	const [inputName, setInputName] = useState('')
	const [inputDescription, setInputDescription] = useState('')

	const onChangeInfo = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, inputType: string) => {
		if (inputType === 'name') {
			setInputName(e.target.value);
			dispatch({
				type: ActionTypes.DATA_STRUCTURE_CHANGE_INFO,
				payload: {
					key: 'name',
					value: e.target.value
				}
			})
		} else if (inputType === 'description') {
			setInputDescription(e.target.value);
			dispatch({
				type: ActionTypes.DATA_STRUCTURE_CHANGE_INFO,
				payload: {
					key: 'description',
					value: e.target.value
				}
			})
		}
	};


	useEffect(() => {
		if (dataStructure) {
			setInputName(name)
			setInputDescription(description)
		}
	}, [dataStructure])

	if (dataStructure) {
		return (
			<Flex vertical gap='middle' style={{ width: '100%' }} >
				<SingleField title="Идентификатор">
					<span>{versionId}</span>
				</SingleField>

				<SingleField title="Используется в">
					{models.map(item => (<span>{item.modelName};</span>))}
				</SingleField>

				<SingleField title="Наименование">
					<Input
						placeholder="Basic usage"
						style={{ width: '40%' }}
						value={inputName}
						onChange={(e) => onChangeInfo(e, 'name')}
					/>
				</SingleField>

				<SingleField title="Краткое описание">
					<Input.TextArea
						placeholder='type some description'
						style={{ width: '40%', resize: 'none' }}
						rows={4}
						value={inputDescription}
						onChange={(e) => onChangeInfo(e, 'description')}
					/>
				</SingleField>
			</Flex>
		)
	}

	else return <Spin />

}

export default DataStructureInfo