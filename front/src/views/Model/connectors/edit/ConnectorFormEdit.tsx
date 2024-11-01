import { useEffect, useState } from 'react'
import { Flex, Space, Table, Typography, message } from 'antd'
import { Formik, Form } from 'formik';
import { useParams } from 'react-router-dom';
import { ConnectorInputController } from '../../../../controllers/ConnectorInputController';
import { useDispatch } from 'react-redux';
import { ConnectorOutputController } from '../../../../controllers/ConnectorOutputController';
import CustomTable from '../../../../components/customTable/CustomTable';
import ConnectorFormInformation from './mainInfo/ConnectorFormInformation';
import { useAppSelector } from '../../../../redux/Store';
import { Connector } from '../../../../shared/entities/Connector/Connector';
import ConnectorAdditionalForm from './additionalInfo/ConnectorAdditionalForm';
import ReturnButton from '../../../../components/ReturnButton/ReturnButton';
import ConnectorLinkTable from './conLinkTable/ConnectorLinkTable';
import InputFromJSON from './jsonInput/JsonInput';
import { ConnectorController } from '../../../../controllers/ConnectorController';

interface DataType {
	id: number;
	name: string;
	fieldType: string;
	maxSize: number;
	allowEmpty: boolean;
	prohibitSpecCharacters: boolean;
	allowArray: boolean;
	maxArray: number;
	children: DataType[] | null;
}

function ConnectorFormEdit({ edit, connectorType }: { edit: boolean, connectorType: 'input' | 'output' }) {


	const { id, fromModel } = useParams();

	const dispatch = useDispatch();

	const connectorController = new ConnectorController(dispatch);


	const inputConnector = useAppSelector(store => store.ConnectorInputReducer.connector)
	const outputConnector = useAppSelector(store => store.ConnectorOutputReducer.connector)



	const [messageApi, contextHolder] = message.useMessage();
	const [defaultInfo, setDefaultInfo] = useState<Connector>()

	const defaultFields: Array<number> = useAppSelector(store => store.ConnectorInputReducer.fieldList);
	const outputIdArray: Array<number> = useAppSelector(store => store.ConnectorOutputReducer.defaultFields)

	const removeIdFromGrid = (grid: DataType[]) => {

		return grid.map((item: any) => {
			const { key, depth, fromParent, index, isLast, newItem, parent, parentId, id, children, srcRelationId, ...rest } = item;
			const updatedId = defaultFields.includes(Number(id)) ? id : -1


			const updatedItem = { ...rest, id: updatedId };

			if (children && Array.isArray(children)) {
				updatedItem.children = removeIdFromGrid(children);
			}

			return updatedItem;
		});
	};




	useEffect(() => {
		if (connectorType) {
			connectorController.get(String(id)).then(res => {
				setDefaultInfo(res.Some)
			})
		}
	}, [connectorType])

	// useEffect(() => {
	// 	if (fromModel && connectorType === 'output')
	// 	inputController.accessField(fromModel, String(id)
	// )
	// }, [])


	return (
		<Space direction='vertical' style={{ padding: 20, width: "100%" }}>

			<ReturnButton tab='2' fromModel={fromModel && fromModel} />

			<Typography.Title level={2}>
				{!edit ? 'Creating a connector to receive and check data from other systems' :
					'Update a connector to receive and check data from other systems'}
			</Typography.Title>
			<Formik initialValues={{}}
				onSubmit={(values: any, actions) => {
					values.connectorPurpose = Object.entries(values.connectorPurpose || {})
						.filter(([key, value]) => value === true)
						.map(([key]) => key);

					values.isCreate = !edit
					values.isTemplate = false
					values.type = connectorType === 'input' ? 'CONNECTOR_INPUT' : 'CONNECTOR_OUTPUT'
					values.extend = {
						id,
						type: "REPLACE"
					}
					values.fields = !values.extend ? [] : removeIdFromGrid(inputConnector?.fields as DataType[])

					if (!values.isCreate) {
						values.versionId = id
					}

					connectorType === 'input' ?
						connectorController.createOrUpdate(values)
							.then(res => {
								if (!res?.None) {
									const successMessage = "Connector " + res.Some + " successfully " + (edit ? "updated!" : "created!");
									messageApi.open({
										type: 'success',
										content: successMessage,
									});
								} else {
									messageApi.open({
										type: 'error',
										content: "Error creating connector",
									});
								}
							})
							.then(() => {
								connectorController.get(String(id)).then(res => {
									setDefaultInfo(res.Some)
								});
							})
						: connectorController.createOrUpdate(values)
							.then(res => {
								if (!res?.None) {
									const successMessage = "Connector " + res.Some + " successfully " + (edit ? "updated!" : "created!");
									messageApi.open({
										type: 'success',
										content: successMessage,
									});
								} else {
									messageApi.open({
										type: 'error',
										content: "Error creating connector",
									});
								}
							})
							.then(() => {
								connectorController.get(String(id)).then(res => {
									setDefaultInfo(res.Some)
								});
							});

					// }
				}}
				validate={values => {
					console.log("values:", values)
				}}
			>
				{({ isSubmitting, errors, touched, handleSubmit }) => (
					<Form onSubmit={handleSubmit} autoComplete="off" >
						{contextHolder}
						<Space direction='vertical' style={{ width: "100%" }}>
							<ConnectorFormInformation
								defaultInfo={defaultInfo && defaultInfo as Connector}
								connectorType={connectorType}
							/>

							<div style={{ width: '100%', borderTop: '1px solid rgba(0, 0, 0, .1)', margin: '20px 0' }}>

							</div>

							<Flex gap='middle' vertical>
								<Typography.Title level={5} style={{ width: '100%' }}>Структура получаемых данных</Typography.Title>
								<div style={{ width: '100%' }}>


									{fromModel && connectorType === 'output' ?
										<ConnectorLinkTable /> :
										<CustomTable isInputConnector={connectorType === 'input' ? true : false} />}
								</div>
							</Flex>

							<ConnectorAdditionalForm connectorType={connectorType} defaultInfo={defaultInfo && defaultInfo as Connector} />
						</Space>
					</Form>
				)}
			</Formik>
		</Space>
	)
}

export default ConnectorFormEdit