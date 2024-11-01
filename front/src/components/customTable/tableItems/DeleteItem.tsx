import React from 'react'
import { Button, message, Popconfirm, Flex } from 'antd';
import { DeleteOutlined } from '@ant-design/icons';
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../../redux/Model/ConnectorInputReducer';

function DeleteItem({ id }: { id: number | string }) {

	const dispatch = useDispatch()
	const confirm = () => {
		// e.stopPropagation()
		console.log(`deleted - ${id}`);
		dispatch({
			type: ActionTypes.CONNECTOR_REMOVE_ROW,
		})
		message.success('Field was deleted');
	};

	const selectItem = (e: React.MouseEvent<HTMLElement>) => {
		e.stopPropagation()
		dispatch({
			type: ActionTypes.CONNECTOR_PICK_ROW,
			payload: {
				field: id
			}
		})
	}

	//e: React.MouseEvent<HTMLElement>

	return (
		<Flex justify='center' align='center'>
			<Popconfirm
				title="Delete the field"
				description="Are you sure to delete this field?"
				onConfirm={confirm}
				okText="Yes"
				cancelText="No"
			>
				<Button danger icon={<DeleteOutlined />} onClick={(e) => { selectItem(e) }}></Button>
			</Popconfirm>
		</Flex>
	)
}

export default DeleteItem