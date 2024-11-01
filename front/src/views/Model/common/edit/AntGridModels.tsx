import React, { FC, useEffect, useState } from 'react'
import { ActionsType, ComponentType } from '../../../../common/constants';

import { ConnectorInputController } from "../../../../controllers/ConnectorInputController";
import { ConnectorOutputController } from "../../../../controllers/ConnectorOutputController";

import { useDispatch } from "react-redux";
import { URLPaths } from '../../../../config/application/URLPaths';
import { RulesController } from "../../../../controllers/RulesController";
import CustomAntTable from '../../../../components/CustomAntTable';
import { DataModelTableType } from '../../../../components/CustomAntTable/types';
import { useAppSelector } from '../../../../redux/Store';
import { useParams } from 'react-router-dom';

import { Connector, IModel, IModelStructComponents } from '../../../../shared/entities/Connector/Connector';
import { inputColumns, outputColumns, rulesColumns } from './columnTitles';
import { Rule } from '../../../../shared/entities/Rule/Rule';

interface GridModelsProps {
	type: ComponentType;
}

const AntGridModels: FC<GridModelsProps> = ({ type }) => {

	const dispatch = useDispatch();
	const userValue = useAppSelector((store) => store.UserReducer?.user);
	const modelStructComponents = useAppSelector((store) => {
		const model = store.ModelReducer.model;

		if (model && 'modelStructComponents' in model) {
			return model.modelStructComponents as IModelStructComponents[];
		}

		// Обработка случая, когда modelStructComponents отсутствует
		return null; // Или другое значение по умолчанию
	});

	const inputConList = useAppSelector(state => state.ConnectorInputReducer.connectors)
	const outputConList = useAppSelector(state => state.ConnectorOutputReducer.connectors)
	const RulesList = useAppSelector(state => state.RuleReducer.rules)

	const [grid, setGrid] = useState<DataModelTableType[]>();
	const [dataSource, setDataSource] = useState<IModelStructComponents[]>()
	const [fullData, setFullData] = useState<Connector[] | null | Rule[]>([])
	const [currentPaginatorPosition, setCurrentPaginatorPosition] = useState(1);
	const [previewPaginatorPosition, setPreviewPaginatorPosition] = useState(1);


	const [columns, setColumns] = useState<{ title: string, dataIndex: string, key: string }[]>()
	const { id } = useParams();

	const getFactoryComponent = () => {
		switch (type) {
			case ComponentType.Input:
				return {
					controller: new ConnectorInputController(dispatch),
					link: URLPaths.IN_CONNECTOR_EDIT.link,
					modelList: "modelConnectorInputList",
				};
			case ComponentType.Output:
				return {
					controller: new ConnectorOutputController(dispatch),
					link: URLPaths.OUT_CONNECTOR_EDIT.link,
					modelList: "modelConnectorOutputList",
				};
			case ComponentType.Rules:
				return {
					controller: new RulesController(dispatch),
					link: URLPaths.RULES_EDIT.link,
					modelList: "modelRuleList",
				};
			default:
				return null;
		}
	};

	const factoryComponent = getFactoryComponent();

	const setData = (type: ComponentType) => {
		switch (type) {
			case ComponentType.Input:
				setDataSource(modelStructComponents?.filter((model: IModelStructComponents) => model.type === 'CONNECTOR_INPUT'));
				setColumns(inputColumns)

				return
			case ComponentType.Output:
				setDataSource(modelStructComponents?.filter((model: IModelStructComponents) => model.type === 'CONNECTOR_OUTPUT'));
				setColumns(outputColumns)
				return
			case ComponentType.Rules:
				setDataSource(modelStructComponents?.filter((model: IModelStructComponents) => model.type === 'RULE'));
				setColumns(rulesColumns)
				return
			default:
				return setDataSource([]);
		}
	}


	useEffect(() => {

		if (id && factoryComponent) {
			if (userValue && grid) {
				if (userValue?.id === undefined) return;
				const reqConfig = {
					params: {
						page: currentPaginatorPosition === previewPaginatorPosition ? 0 : currentPaginatorPosition > 0 ? currentPaginatorPosition - 1 : 0,
						size: 100,
						userId: parseInt(userValue.id, 10),
					},
				};

				const { controller, modelList, link } = factoryComponent;
				controller.getAll(reqConfig?.params, { modelId: id })
					.then((res) => {
						setGrid(res.Some)
					});

			}
		}

		setData(type)

		// }, [grid, type, modelStructComponents, columns, inputConList, outputConList, RulesList]);
	}, [type, modelStructComponents, inputConList, outputConList, RulesList, grid])


	return (
		<CustomAntTable
			cols={columns !== undefined ? columns : []}
			grid={grid}
			dataSource={dataSource}
		// fullData={fullData}
		/>
	)
}

export default AntGridModels