import { useState } from "react"

import { Flex, Button, Input, Tooltip, Popover } from 'antd'
import { EditOutlined, SaveOutlined } from '@ant-design/icons';
import { useDispatch } from "react-redux";
import { ActionTypes as InputConActionTypes } from "../../../redux/Model/ConnectorInputReducer";
import { ActionTypes as OutputConActionTypes } from "../../../redux/Model/ConnectorOuputReducer";
import { useAppSelector } from "../../../redux/Store";
const InputItem = ({ defaultValue, id, type, newItem, pickField }: { defaultValue: string, id: number | string, type?: string, newItem?: boolean, pickField: () => void }) => {
	const [isEdit, setIsEdit] = useState(newItem && newItem === true ? true : false)
	const [value, setValue] = useState(defaultValue)
	const [description, setDescription] = useState('description')

	const dispatch = useDispatch()

	const setNewField = (e: React.ChangeEvent<HTMLInputElement>) => {
		if (e.target.value.length === 0) {
			setValue(defaultValue)
		}
		setValue(e.target.value)
	}

	const content = (
		<div onClick={(e) => e.stopPropagation()}>
			<Input.TextArea value={description} onChange={(e) => setDescription(e.target.value)} onClick={(e) => e.stopPropagation()} />
		</div>
	);

	const handleField = (e: any) => {
		e.stopPropagation()
		if (isEdit) {
			dispatch({
				type: type === 'input' ? InputConActionTypes.CONNECTOR_UPDATE_ROW : OutputConActionTypes.CONNECTOR_UPDATE_ROW,
				payload: {
					key: "name",
					data: value,
				},
			})
			dispatch({
				type: type === 'input' ? InputConActionTypes.CONNECTOR_UPDATE_ROW : OutputConActionTypes.CONNECTOR_UPDATE_ROW,
				payload: {
					key: "description",
					data: description,
				},
			})
			dispatch({
				type: type === 'input' ? InputConActionTypes.CONNECTOR_UPDATE_ROW : OutputConActionTypes.CONNECTOR_UPDATE_ROW,
				payload: {
					key: "newItem",
					data: false,
				},
			})
			setIsEdit(false);
		} else {
			dispatch({
				type: type === 'input' ? InputConActionTypes.CONNECTOR_PICK_ROW : OutputConActionTypes.CONNECTOR_PICK_ROW,
				payload: {
					field: id
				}
			})
			setIsEdit(true);
		}
	};

	return (
		<Flex justify='space-between' align='center' gap='middle'>
			{
				isEdit || newItem === true ?
					<Popover content={content} title="Set Description" trigger="click" placement="topLeft">
						<Input
							onChange={(e) => setNewField(e)}
							value={value}
							onClick={(e) => {
								e.stopPropagation()
								pickField()
							}}
							style={{ borderRadius: '6px', height: 32 }}
						/>
					</Popover>
					:
					<Tooltip placement="top" title='description'>
						<span>
							{value}
						</span>
					</Tooltip>}


			<Button
				shape="circle"
				type="text"
				icon={isEdit || newItem === true ? <SaveOutlined /> : <EditOutlined />}
				onClick={(e) => { handleField(e) }}
			/>
		</Flex>
	)
}

export default InputItem