import { useEffect, useState } from 'react'
import TableHeader from './TableHeader'
import { Flex } from 'antd'
import SortableTreeTable from './SortableTree'
import ControlElements from './ControlElements'
import { useAppSelector } from '../../redux/Store'
import { DataType } from './types'
import { useDispatch } from 'react-redux'
import { ActionTypes } from '../../redux/Model/ConnectorInputReducer'
import { ActionTypes as OutputConTypes } from '../../redux/Model/ConnectorOuputReducer'


function CustomTable({ isInputConnector }: { isInputConnector: boolean }) {
	const dispatch = useDispatch()

	const inputConnector = useAppSelector(store => store.ConnectorInputReducer.connector);
	const outputConnector = useAppSelector(store => store.ConnectorInputReducer.connector);

	const [grid, setGrid] = useState<DataType[]>([]);

	useEffect(() => {
		console.log(isInputConnector === true ? 'true' : 'false')
		if (isInputConnector && inputConnector !== null && inputConnector.fields !== undefined) {

			console.log('input', inputConnector)
			const updatedFields = inputConnector.fields.map(field => ({
				...field,
				key: field.id
			}));
			setGrid(updatedFields);

		}
		else
			if (!isInputConnector && outputConnector !== null && outputConnector.fields !== undefined) {
				console.log('output', outputConnector)
				const updatedFields = outputConnector.fields.map(field => ({
					...field,
					key: field.id
				}));
				setGrid(updatedFields);

			}

	}, [inputConnector, outputConnector, isInputConnector]);

	const updateTableRowsPosition = (value: DataType[]) => {

		setGrid(value)

		// if (isInputConnector)
		dispatch({
			type: ActionTypes.CONNECTOR_IN_CHANGE_POS,
			payload: value
		})
		// else
		// dispatch({
		// 	type: OutputConTypes.CONNECTOR_OUT_CHANGE_POS,
		// 	payload: value
		// })

	}


	return (
		<Flex vertical style={{ width: '100%', minWidth: '800px' }}>
			<ControlElements grid={grid} isInputConnector={isInputConnector} />
			<TableHeader />
			<SortableTreeTable
				data={grid}
				updatePos={(newPos: DataType[]) => updateTableRowsPosition(newPos)}
				isInputConnector={isInputConnector}
			/>
		</Flex>
	)
}

export default CustomTable