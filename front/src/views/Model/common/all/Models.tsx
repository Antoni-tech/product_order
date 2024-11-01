import React, { FC, useEffect, useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, useParams } from "react-router";
import { useAppSelector } from "../../../../redux/Store";
import { Grid as GridDHX } from "dhx-suite";
import { URLPaths } from "../../../../config/application/URLPaths";

import { Toolbar as ToolbarDHX } from "dhx-suite";
import { ModelsController } from "../../../../controllers/ModelsController";

import { Flex, Button, message } from 'antd'
import { PlusOutlined, CopyOutlined } from '@ant-design/icons';
import { Connector } from "../../../../shared/entities/Connector/Connector";
import ModelsCustomTable from "./ModelsCustomTable/ModelsCustomTable";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faClone } from "@fortawesome/free-regular-svg-icons";


export interface TableParams {
	page: number,
	size: number,
	userId: number
}

export const Models: FC = () => {
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const { id } = useParams();

	const userValue = useAppSelector(store => store.UserReducer?.user)
	const models = useAppSelector(store => store.ModelReducer.models?.list)
	const modelsController = new ModelsController(dispatch);
	// const [currentPaginatorPosition, setCurrentPaginatorPosition] = useState(1)
	// const [previewPaginatorPosition, setPreviewPaginatorPosition] = useState(1)
	const [filterConfig] = useState<any>()
	const [grid, setGrid] = useState<GridDHX>();
	const [toolbar, setToolbar] = useState<ToolbarDHX>();
	const gridConnectorsRef = useRef<HTMLDivElement | null>(null);
	const toolBarElRef = useRef<HTMLDivElement>(null);

	const [dataSource, setDataSource] = useState<Connector[]>([])
	const [messageApi, contextHolder] = message.useMessage();
	const [pickedId, setPickedId] = useState<string>('')
	const [uniqId, setUniqId] = useState(0)


	const [tableParams, setTableParams] = useState<TableParams>({
		page: 0,
		size: 10,
		userId: 1
	});

	const pickIdFromTable = (id: number) => {
		setPickedId(String(id))
	}
	const loadAllModels = () => {
		if (userValue) {
			if (userValue?.id === undefined) return;
			modelsController.getModelAll(tableParams).then(res => {
				grid?.data.parse(res.Some);
			})
		}
	}

	const addNewModel = () => {

		const newId = models ? models.length + 1 : 0;

		const newModel = {
			name: `Model #${newId}`,
			isCreate: true,
			inputs: [],
			outputs: [],
			rules: []
		}

		modelsController.createOrUpdateModel(newModel)
			.then(res => {
				if (!res?.None) {
					messageApi.open({
						type: 'success',
						content: 'New model added successfully',
					});
					loadAllModels()
				} else {
					messageApi.open({
						type: 'error',
						content: 'Error create connector',
					});
				}
			})
		setUniqId(newId);
	}

	const duplicateModel = () => {
		modelsController.duplicateModel(pickedId)
			.then(res => {
				if (!res?.None) {
					messageApi.open({
						type: 'success',
						content: 'Model was duplicated',
					});
					loadAllModels()
				} else {
					messageApi.open({
						type: 'error',
						content: 'Error create connector',
					});
				}
			})

	}

	useEffect(() => {
		// if (userValue && grid) {
		loadAllModels()

	}, [filterConfig, userValue, id, grid, uniqId])

	useEffect(() => {
		if (models) { setDataSource(models) }
	}, [models, uniqId])


	useEffect(() => {

		if (toolBarElRef.current && !toolbar) {
			const newToolbar = new ToolbarDHX(toolBarElRef.current, {
				css: "toolbar_template_a"
			});

			newToolbar.data.add({ id: "add_model", type: "button", value: "Add Model" });
			newToolbar.events.on('click', (id) => {
				if (id === "add_model") {
					navigate(`${URLPaths.MODEL_CREATE.link}`)
				}
			});
			setToolbar(newToolbar);
		}

		if (gridConnectorsRef.current && !grid) {
			const newGrid = new GridDHX(gridConnectorsRef.current, {
				columns: [
					{ id: "versionId", header: [{ text: "Id" }], type: "string" },
					{ id: "name", header: [{ text: "Name" }], type: "string" },
					{ id: "description", header: [{ text: "Description" }], type: "string" },
					{ id: "type", header: [{ text: "Type" }], type: "string" },
					{
						id: "action", gravity: 1.5, header: [{ text: "Actions", align: "center" }],
						htmlEnable: true,
						align: "center",
						resizable: true,
						template: function () {
							return "<span class='action-buttons'>" +
								"<a class='edit-button'>Edit</a>" +
								"<a class='remove-button'>Delete</a>" +
								"</span>"
						}
					}
				],
				editable: true,
				autoWidth: true,
				eventHandlers: {
					onclick: {
						"remove-button": function (e, data) {
							console.log("remove-button", data)
							if (data.row.id) {
								newGrid?.data.remove(data.row.id);
							}
						},
						"edit-button": function (e, data) {
							navigate(`${URLPaths.MODEL_EDIT.link}/${data.row.versionId}`)
						}
					}
				}
			});
			setGrid(newGrid);
		}
	}, [grid, toolbar])

	return (
		// <>
		// 	<Box {...BoxStyleConfig}>
		// 		<Box ml={"auto"} ref={toolBarElRef} />
		// 		<Box style={{ height: "700px", width: "100%", }} ref={gridConnectorsRef} />
		// 	</Box>
		// </>
		<Flex gap='middle' vertical align='flex-start' style={{ position: 'relative' }}>
			{contextHolder}
			<Flex gap='small' style={{ position: 'absolute', top: 15, left: 0, zIndex: 5 }}>
				<Button onClick={addNewModel} icon={<PlusOutlined />} />
				<Button onClick={duplicateModel} icon={<FontAwesomeIcon icon={faClone} />} />
			</Flex>
			<ModelsCustomTable
				type="MODEL"
				dataSource={dataSource}
				pickIdFromTable={pickIdFromTable}
				requestParams={tableParams}
			/>
		</Flex>
	);
}
export default Models;
