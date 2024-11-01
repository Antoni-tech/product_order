import { Flex, Button } from 'antd'
import { PlusOutlined, MinusOutlined } from '@ant-design/icons';
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../../../../redux/Model/DataStructureReducer';
import { useAppSelector } from '../../../../../redux/Store';

function ControlPanel({ activateEditMode }: { activateEditMode: () => void }) {

	const dispatch = useDispatch()
	const fields = useAppSelector(store => store.DataStructureReducer.dataStructure?.fields)
	const selectedField = useAppSelector(store => store.DataStructureReducer.selectedField)
	const addRow = () => {

		const newField = {
			id: fields?.length ? fields?.length + 1 : 1,
			name: `New Field #${fields?.length ? fields?.length + 1 : 1}`,
			fieldType: 'STRING',
			defaultField: false,
			maxArray: 255,
			testValueJson: null,
			srcRelationId: null,
			children: null
		}

		dispatch({
			type: ActionTypes.DATA_STRUCTURE_FIELDS_ADD_ROW,
			payload: newField
		})
		dispatch({
			type: ActionTypes.DATA_STRUCTURE_FIELDS_SELECT_ROW,
			payload: newField
		})
		activateEditMode()
	}

	const removeRow = () => {
		dispatch({
			type: ActionTypes.DATA_STRUCTURE_FIELDS_REMOVE_ROW,
			payload: selectedField?.id
		})
	}

	return (
		<Flex gap='middle'>
			<Button type="primary" icon={<PlusOutlined />} onClick={addRow} />
			<Button type="primary" icon={<MinusOutlined />} onClick={removeRow} />
		</Flex>
	)
}

export default ControlPanel