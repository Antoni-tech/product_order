import { Form, Formik } from "formik";
import React, { FC, useEffect } from "react";
import { useDispatch } from "react-redux";
import { Box, Heading } from "@chakra-ui/react";
import { BoxStyleConfig } from "../../../Forms/FormStyleConfigs";
import { Card } from "../../../../components/Card/Card";
import { ModalService } from "../../../../service/Modal/ModalService";
import { useParams } from "react-router";
import { LabelStyled } from "../../../../components/Inputs/LabelStyled";
import DHXFormModels from "./DHXFormModels";
import { MainButton } from "../../../../components/Buttons/MainButton";
import { useAppSelector } from "../../../../redux/Store";
import { ModelsController } from "../../../../controllers/ModelsController";
import GridModels from "./GridModels";
import { ComponentType } from "../../../../common/constants";
import CustomAntTable from "../../../../components/CustomAntTable";
import AntGridModels from "./AntGridModels";
import ModelInfo from "./modelInfo";
import { CaretRightOutlined } from '@ant-design/icons';

import { Flex, message, Collapse, Typography, ConfigProvider, Spin } from 'antd'
import ModelTableControl from "./ModelTableControl";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faChevronRight, faArrowRightLong } from '@fortawesome/free-solid-svg-icons'
import ModelTesting from "./ModelTesting";

import styles from './editModel.module.scss'
import { ActionTypes } from "../../../../redux/Model/ModelReducer";
import ReturnButton from "../../../../components/ReturnButton/ReturnButton";
import IncidentsTable from "./incidents/IncidentsTable";

export const FormModels: FC<{ edit: boolean }> = (edit) => {
	const dispatch = useDispatch();
	const ConnectorFormCardConfig = { position: "relative", width: "100%" }
	const modalService = new ModalService(dispatch);
	const { id } = useParams();
	const modelsController = new ModelsController(dispatch);
	const model = useAppSelector(store => store.ModelReducer.model)
	const activeModelType = useAppSelector(store => store.ModelReducer.selectedModelField?.type)

	const [messageApi, contextHolder] = message.useMessage();

	// useEffect(() => {

	// }, [model]);

	useEffect(() => {
		if (id !== null && id !== undefined) {
			modelsController.getModel(id).then(() => {
			})
		}
	}, [id]);

	return (
		<Card {...ConnectorFormCardConfig} style={{ padding: 20 }}>

			<ReturnButton tab='1' />

			<Heading as='h4' mb={"30px"} >
				{!edit.edit ? 'Создание модели' :
					'Обновление модели'}</Heading>
			<Formik initialValues={{}}
				onSubmit={(values: any, actions) => {
					values.isCreate = !edit.edit
					values.isTemplate = false
					if (!values.isCreate) {
						values.versionId = Number(id)
					}
					values.name = model?.name
					values.type = model?.type
					values.description = model?.description
					values.inputs = model?.modelStructComponents.filter(element => element.type === 'CONNECTOR_INPUT')
						.map(({ daysRemaining, resultIncremental, launchSecondStage, modelComponentId }) => ({ daysRemaining: 0, resultIncremental: true, launchSecondStage, componentId: modelComponentId }));
					values.outputs = model?.modelStructComponents.filter(element => element.type === 'CONNECTOR_OUTPUT')
						.map(({ daysRemaining, resultIncremental, modelComponentId }) => ({ daysRemaining: 0, resultIncremental: true, componentId: modelComponentId }));
					values.rules = model?.modelStructComponents.filter(element => element.type === 'RULE')
						.map(({ daysRemaining, resultIncremental, modelComponentId, queueNumber }) => ({ daysRemaining: 0, resultIncremental: true, componentId: modelComponentId, queueNumber }))
					modelsController.createOrUpdateModel(values)
						.then(res => {
							if (!res?.None) {
								messageApi.open({
									type: 'success',
									content: 'Model was updated',
								});
							} else {
								messageApi.open({
									type: 'error',
									content: 'Error',
								});
							}
						}).catch(error => {

							const errorMessage = error || 'Unknown error occurred';
							messageApi.open({
								type: 'error',
								content: `${errorMessage}`,
							});
						})
						.then(() => modelsController.getModel(String(model?.versionId)))
				}}
				validate={values => {
					console.log("values:", values)
				}}
			>
				{({ isSubmitting, errors, touched, handleSubmit }) => (
					<ConfigProvider
						theme={{
							components: {
								Collapse: {
									// contentPadding: '16px 8px',
									contentPadding: 0,
									headerPadding: 0,
									borderRadius: 8,
								},
							},
						}}
					>

						<Form onSubmit={handleSubmit} autoComplete="off" >
							{contextHolder}
							{/* <DHXFormModels /> */}
							<ModelInfo />
							{/* <Box {...BoxStyleConfig} > */}

							<ModelTableControl />

							<Flex vertical style={{ width: '100%', marginTop: 25, marginBottom: 25 }}>
								<Flex justify="space-between" style={{ width: '100%' }}>

									<Collapse
										// collapsible="header"
										collapsible="icon"
										expandIconPosition="end"
										className={styles.collapseContainer}
										expandIcon={({ isActive }) => <div className={styles.collapseArrow}><CaretRightOutlined rotate={isActive ? 90 : 0} /></div>}

										defaultActiveKey={1}
										items={[
											{
												key: '1',
												label:
													<Typography.Text
														ellipsis
														onClick={() => {
															dispatch({
																type: ActionTypes.MODEL_SELECT_FIELD,
																payload:
																	model && model.modelStructComponents.length && model?.modelStructComponents.length > 0 ?
																		model?.modelStructComponents.filter(element => element.type === 'CONNECTOR_INPUT')[0] :
																		{
																			type: 'CONNECTOR_INPUT'
																		}
															})
														}} >Получение данных</Typography.Text>
												,
												children: <AntGridModels type={ComponentType.Input} />,
												// showArrow: false,
												headerClass: activeModelType === 'CONNECTOR_INPUT' ? styles.collapseActiveHeader + ' ' + styles.collapseHeader : styles.collapseHeader,
											},
										]}
									/>

									<div className={styles.arrow}>
										<div className={styles.line}></div>
										<FontAwesomeIcon icon={faArrowRightLong} style={{ color: '#000' }} />
									</div>

									<Collapse
										collapsible="icon"
										expandIconPosition="end"
										expandIcon={({ isActive }) => <div className={styles.collapseArrow}><CaretRightOutlined rotate={isActive ? 90 : 0} /></div>}

										className={styles.collapseContainer}
										defaultActiveKey={2}
										items={[
											{
												key: '2',
												label: <Typography.Text
													ellipsis
													onClick={() => {
														dispatch({
															type: ActionTypes.MODEL_SELECT_FIELD,
															payload: model && model.modelStructComponents.length && model.modelStructComponents.length > 0 ?
																model.modelStructComponents.find(element => element.type === 'RULE') || { type: 'RULE' } :
																{ type: 'RULE' }
														})

													}} >Правила вычисления,выявления <br /> и реагирования</Typography.Text>,
												children: <AntGridModels type={ComponentType.Rules} />,
												// showArrow: false,
												headerClass: activeModelType === 'RULE' ? styles.collapseActiveHeader + ' ' + styles.collapseHeader : styles.collapseHeader,
											},
										]}
									/>

									<div className={styles.arrow}>
										<div className={styles.line}></div>
										<FontAwesomeIcon icon={faArrowRightLong} style={{ color: '#000' }} />
									</div>

									<Collapse
										collapsible="icon"
										expandIconPosition="end"
										expandIcon={({ isActive }) => <div className={styles.collapseArrow}><CaretRightOutlined rotate={isActive ? 90 : 0} /></div>}

										className={styles.collapseContainer}
										defaultActiveKey={3}
										items={[
											{
												key: '3',
												label: <Typography.Text
													ellipsis
													onClick={() => {
														dispatch({
															type: ActionTypes.MODEL_SELECT_FIELD,
															payload: model && model.modelStructComponents.length && model.modelStructComponents.length > 0 ?
																model.modelStructComponents.find(element => element.type === 'CONNECTOR_OUTPUT') || { type: 'CONNECTOR_OUTPUT' } :
																{ type: 'CONNECTOR_OUTPUT' }
														})


													}} >Отправка данных</Typography.Text>,
												children: <AntGridModels type={ComponentType.Output} />,
												// showArrow: false,
												headerClass: activeModelType === 'CONNECTOR_OUTPUT' ? styles.collapseActiveHeader + ' ' + styles.collapseHeader : styles.collapseHeader,

											},
										]}

									/>

								</Flex>

							</Flex>

							<ModelTesting />

							<IncidentsTable />

							{/* </Box> */}
						</Form>
					</ConfigProvider>
				)}
			</Formik>
		</Card>
	);


}
