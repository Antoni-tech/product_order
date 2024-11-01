
import { Button, Flex, FloatButton, message } from 'antd'
import { useFormikContext } from 'formik';
import { useAppSelector } from '../../redux/Store';
import { useDispatch } from 'react-redux';
import { ActionTypes as inputConActionTypes } from '../../redux/Model/ConnectorInputReducer';
import { ActionTypes as outputConActionTypes } from '../../redux/Model/ConnectorOuputReducer'
import { DataType } from './types';
import { PlusOutlined, MinusOutlined } from '@ant-design/icons';
import { SaveOutlined } from '@ant-design/icons';
import { ConnectorInputController } from '../../controllers/ConnectorInputController';
import { ConnectorOutputController } from '../../controllers/ConnectorOutputController';
import { useParams } from 'react-router-dom';
import InputFromJSON from '../../views/Model/connectors/edit/jsonInput/JsonInput';
import SelectableStructure from '../../views/Model/rules/edit/dataStructureTable/selectableStructure';
import { ConnectorController } from '../../controllers/ConnectorController';


interface fieldType {
	id: number;
	name: string;
	fieldType: string;
	maxSize: number;
	key?: number;
	allowEmpty: boolean;
	prohibitSpecCharacters: boolean;
	allowArray: boolean;
	maxArray: number;
	children: fieldType[] | null;
}

function ControlElements({ grid, isInputConnector }: { grid: DataType[], isInputConnector: boolean }) {
	const formik = useFormikContext();
	const dispatch = useDispatch();
	const connector = useAppSelector(store => store.ConnectorInputReducer.connector);
	const outConnector = useAppSelector(store => store.ConnectorOutputReducer.connector);

	const currentCon = useAppSelector(store => store.ConnectorInputReducer.fieldResponse)
	const currentOutputCon = useAppSelector(store => store.ConnectorOutputReducer.fieldResponse)

	const { id } = useParams();

	const connectorController = new ConnectorController(dispatch);
	// const outputController = new ConnectorOutputController(dispatch);

	const [messageApi, contextHolder] = message.useMessage();


	const addField = (type: boolean) => {

		const countFields = (fields: fieldType[]): number => {
			let count = 0;
			fields.forEach(field => {
				count++; // Учитываем текущее поле
				if (field.children) {
					count += countFields(field.children); // Рекурсивно учитываем дочерние поля
				}
			});
			return count;
		};

		if (type === true) {
			dispatch({
				type: inputConActionTypes.CONNECTOR_IN_ADD_ROW,
				payload: {
					name: `newField${connector && countFields(connector.fields) + 1}`,
					id: connector ? countFields(connector.fields) : 0,
					fieldType: "STRING",
					maxSize: 255,
					allowEmpty: true,
					prohibitSpecCharacters: true,
					allowArray: false,
					maxArray: 0,
					newItem: true,

					fromParent: currentCon ? currentCon.id : null
				},
			});
		}
		else {
			dispatch({
				type: outputConActionTypes.CONNECTOR_OUT_ADD_ROW,
				payload: {
					name: `newField${outConnector && countFields(outConnector.fields) + 1}`,
					id: outConnector ? countFields(outConnector.fields) : 0,
					fieldType: "STRING",
					maxSize: 255,
					allowEmpty: true,
					prohibitSpecCharacters: true,
					allowArray: false,
					maxArray: 0,
					newItem: true,

					fromParent: currentOutputCon ? currentOutputCon.id : null
				},
			});
		}

	};


	const saveChanges = async () => {

		if (grid && isInputConnector === true) {
			dispatch({
				type: inputConActionTypes.CONNECTOR_IN_CHANGE_POS,
				payload: grid
			})

			formik.setFieldValue("fields", removeIdFromGrid(grid));
			formik.handleSubmit()
			await new Promise(resolve => setTimeout(resolve, 1000));
			connectorController.get(String(id)).then(res => {
			})
			messageApi.open({
				type: 'success',
				content: 'This is connector was updated',
			});
		}

		if (grid && isInputConnector === false) {

			dispatch({
				type: outputConActionTypes.CONNECTOR_OUT_CHANGE_POS,
				payload: grid
			})

			formik.setFieldValue("fields", removeIdFromGrid(grid));
			formik.handleSubmit();
			await new Promise(resolve => setTimeout(resolve, 1000));
			connectorController.get(String(id)).then(res => {
			})
		}

	};

	const removeIdFromGrid = (grid: any[]) => {
		return grid.map((item: any) => {
			const { children, ...rest } = item;
			const updatedItem = { id: -1, ...rest };

			if (children && Array.isArray(children)) {
				updatedItem.children = removeIdFromGrid(children);
			}

			return updatedItem;
		});
	};


	return (

		<Flex gap='middle' style={{ width: '100%', paddingBottom: '20px' }} justify='space-between'>

			{contextHolder}

			<Flex gap='middle' style={{ width: '300px' }} >

				<Button type="primary" icon={<PlusOutlined />} onClick={() => addField(isInputConnector === true ? true : false)} />


				<Button type="primary" icon={<MinusOutlined />} onClick={() => {
					isInputConnector === true ?
						dispatch({
							type: inputConActionTypes.CONNECTOR_REMOVE_ROW,
						})
						:
						dispatch({
							type: outputConActionTypes.CONNECTOR_OUT_REMOVE_ROW,
						})
				}} />

				<InputFromJSON isInputConnector={isInputConnector} />

				<SelectableStructure />

			</Flex>

			<FloatButton icon={<SaveOutlined />} onClick={saveChanges} />




		</Flex >

	)
}

export default ControlElements