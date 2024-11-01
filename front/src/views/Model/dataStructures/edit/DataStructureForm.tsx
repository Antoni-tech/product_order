import { FloatButton, Space, Typography, message } from 'antd'
import React, { useEffect } from 'react'
import DataStructureInfo from './formData/DataStructureInfo'
import DataStructureTable from './table/DataStructureTable'
import { Form, Formik } from 'formik'
import { useParams } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { DataStructureController } from '../../../../controllers/DataStructureController'
import { useAppSelector } from '../../../../redux/Store'
import { SaveOutlined } from '@ant-design/icons';
import ReturnButton from '../../../../components/ReturnButton/ReturnButton'

function DataStructureForm({ edit }: { edit: boolean }) {

	const { id, fromModel } = useParams()
	const dispatch = useDispatch()
	const dataStructureController = new DataStructureController(dispatch);
	const [messageApi, contextHolder] = message.useMessage();
	const { dataStructure, defaultFields } = useAppSelector(store => store.DataStructureReducer)


	useEffect(() => {
		if (id !== null && id !== undefined) {
			dataStructureController.get(String(id)).then(res => { })
		}
	}, [id])

	return (
		<Space direction="vertical" size="middle" style={{ display: 'flex', width: '100%', padding: 20 }}>

			<Typography.Title level={2}>
				{!edit ? 'Creating a data Structure' :
					'Update a data Structure'}
			</Typography.Title>

			<Formik initialValues={{}}
				onSubmit={(values: any, actions) => {
					values.isCreate = !edit
					values.isTemplate = false
					values.fields = dataStructure?.fields.map((field) => ({
						...field,
						id: defaultFields && defaultFields.includes(field.id) ? field.id : -1
					}))

					values.name = dataStructure?.name
					values.description = dataStructure?.description
					if (!values.isCreate) {
						values.versionId = id
					}

					if (values.fields.length === 0) {
						messageApi.open({
							type: 'error',
							content: "Add at least one field to data Structure",
						});
					} else {
						dataStructureController.createOrUpdate(values)
							.then(res => {
								if (!res?.None) {
									const successMessage = "Data Structure " + res.Some + " successfully" + edit ? "updated!" : "created!";
									messageApi.open({
										type: 'success',
										content: successMessage,
									});
								} else {
									messageApi.open({
										type: 'error',
										content: "Error create connector",
									});
								}
							})
							.then(() => dataStructureController.get(String(id)).then(res => { }))
					}
				}}
				validate={values => {
					console.log("values:", values)
				}}
			>
				{({ handleSubmit, submitForm }) => (
					<Form onSubmit={handleSubmit} autoComplete="off" >
						{contextHolder}
						<Space direction='vertical' style={{ width: "100%" }}>

							<DataStructureInfo />
							<DataStructureTable />

							<FloatButton onClick={submitForm} icon={<SaveOutlined />} />
							<ReturnButton tab='4' fromModel={fromModel && fromModel} />

						</Space>
					</Form>
				)}
			</Formik>

		</Space>
	)
}

export default DataStructureForm