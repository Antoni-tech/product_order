import React, { FC, useEffect, useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, useParams } from "react-router";
import { Box } from "@chakra-ui/react";
import { Grid as GridDHX } from "dhx-suite";
import { Modal, Space, Flex, Radio, Select, Button, message, Input } from 'antd'

import { Toolbar as ToolbarDHX } from "dhx-suite";
import { useAppSelector } from "../../../../redux/Store";
import { RulesController } from "../../../../controllers/RulesController";
import { URLPaths } from "../../../../config/application/URLPaths";
import { BoxStyleConfig } from "../../../Forms/FormStyleConfigs";
import { ActionTypes } from "../../../../redux/Model/RuleReducer";
import { Rule } from "../../../../shared/entities/Rule/Rule";
import ModelsCustomTable from "../../common/all/ModelsCustomTable/ModelsCustomTable";
import { PlusOutlined, CopyOutlined } from '@ant-design/icons';
import { TableParams } from "../../common/all/Models";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faClone } from "@fortawesome/free-regular-svg-icons";

export const Rules: FC = () => {
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const { id } = useParams();

	const userValue = useAppSelector(store => store.UserReducer?.user)
	const rules = useAppSelector(store => store.RuleReducer.rules?.list)
	const rulesController = new RulesController(dispatch);
	const [currentPaginatorPosition, setCurrentPaginatorPosition] = useState(1)
	const [previewPaginatorPosition, setPreviewPaginatorPosition] = useState(1)
	const [filterConfig] = useState<any>()
	const [grid, setGrid] = useState<GridDHX>();
	const [toolbar, setToolbar] = useState<ToolbarDHX>();
	const gridRulesRef = useRef<HTMLDivElement | null>(null);
	const toolBarElRef = useRef<HTMLDivElement>(null);

	const [isModalOpen, setIsModalOpen] = useState(false);
	const [selectValue, setSelectValue] = useState('QUANTITY')
	const [checkBoxValue, setCheckBoxValue] = useState(1);
	const [pickedId, setPickedId] = useState<string>('')
	const [messageApi, contextHolder] = message.useMessage();
	const [dataSource, setDataSource] = useState<Rule[]>([])
	const [newConName, setConName] = useState<string>()

	const tableParams: TableParams = {
		page: 0,
		size: 10,
		userId: 1
	};

	const getAllRules = () => {
		if (userValue) {
			if (userValue?.id === undefined) return;
			// const reqConfig = {
			// 	params: {
			// 		page: (currentPaginatorPosition === previewPaginatorPosition) ? 0 : (currentPaginatorPosition > 0) ? currentPaginatorPosition - 1 : 0,
			// 		size: 100,
			// 		userId: parseInt(userValue.id, 10),
			// 	}
			// }
			rulesController.getAll(tableParams, null).then(res => {
				// grid?.data.parse(res.Some);
			})
		}
	}
	const handleSelect = (value: string) => {
		setSelectValue(value);
	};

	const showModal = () => {
		setIsModalOpen(true);
	};

	const handleOk = () => {
		setIsModalOpen(false);

		const newRule = {
			name: newConName,
			summarySubType: selectValue,
			jsonData: selectValue === 'QUANTITY' ? [{
				condition: "",
				resultCondition: ""
			}] : [],
			fields: [],
			isCreate: true,
		}

		if (selectValue === 'QUANTITY') {
			dispatch({
				type: ActionTypes.RULE_SELECT_JSON_DATA,
				payload: {
				}
			})
		} else {
			dispatch({
				type: ActionTypes.RULE_SELECT_JSON_DATA,
				payload: {

				}
			})
		}
		rulesController.createOrUpdate(newRule).then(res => {
			if (!res?.None) {
				messageApi.open({
					type: 'success',
					content: 'New Rule added successfully',
				});
				getAllRules()
			} else {
				messageApi.open({
					type: 'error',
					content: 'Error create Rule',
				});
			}
		})

	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	const duplicate = () => {
		rulesController.duplicate(pickedId)
			.then(res => {
				if (!res?.None) {
					messageApi.open({
						type: 'success',
						content: `Rule id:${pickedId} was duplicated`,
					});
					getAllRules()
				} else {
					messageApi.open({
						type: 'error',
						content: 'Error create rule',
					});
				}
			})
	}

	const pickIdFromTable = (id: number) => {
		setPickedId(String(id))
	}

	useEffect(() => {
		getAllRules()
		setConName(`Rule #${totalRulesNumber}`)
	}, [filterConfig, userValue, currentPaginatorPosition, grid])

	useEffect(() => {
		if (rules) { setDataSource(rules) }
	}, [rules])

	const totalRulesNumber = rules && rules.length > 0 ? rules.length + 1 : 0


	return (
		<Flex vertical gap='middle' align="flex-end" style={{ position: 'relative' }}>
			<Modal title="Создание правила" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
				<Space direction="vertical" size="middle">

					<Flex vertical gap='middle'>
						<span>Название</span>
						<Input value={newConName} onChange={(e) => setConName(e.target.value)} />
					</Flex>

					<Flex vertical gap='middle'>
						<span>Вид</span>
						<Radio.Group value={checkBoxValue}>
							<Flex vertical gap='middle'>
								<Radio value={1} defaultChecked>Обычное</Radio>
								<Radio value={2} disabled>ML с обучением</Radio>
								<Radio value={3} disabled>ML без обучения</Radio>
								<Radio value={4} disabled>Нейронная сеть</Radio>
							</Flex>
						</Radio.Group>
					</Flex>

					<Flex vertical gap='middle'>
						<span>Тип данных результата</span>
						<Select
							// value={rule && rule.ruleType}
							defaultValue={selectValue}
							onChange={handleSelect}
							options={[{
								label: 'Доступные',
								options: [
									{ value: 'QUANTITY', label: 'Количество' },
									{ value: 'QUALITY', label: 'Качество' },
								]
							},
							{
								label: 'Недоступные',
								options: [

									{ value: 'QUANTITY_ACC', label: 'Количество аккумулятивных', disabled: true },
									{ value: 'STRING', label: 'Строка', disabled: true },
									{ value: 'ARRAY', label: 'Массив', disabled: true },
								]
							}

							]}
						/>

					</Flex>
				</Space>
			</Modal>

			<Flex gap='small' style={{ position: 'absolute', top: 15, left: 0, zIndex: 5 }}>
				<Button onClick={showModal} icon={<PlusOutlined />} />
				<Button onClick={duplicate} icon={<FontAwesomeIcon icon={faClone} />} />
			</Flex>

			<ModelsCustomTable
				type="RULE"
				dataSource={dataSource}
				pickIdFromTable={pickIdFromTable}
				requestParams={tableParams}
			/>

		</Flex>
	);
}
export default Rules;
