import { FC } from "react";

import Models from "./Models";
import Rules from "../../rules/all/Rules";

import { Flex, Tabs } from 'antd';
import type { TabsProps } from 'antd';
import DataStructureList from "../../dataStructures/all/DataStructure";
import Connectors from "../../connectors/all/Connectors";
import { useAppSelector } from "../../../../redux/Store";

const ModelsManage: FC = () => {

	const currentTab = useAppSelector(store => store.ModelReducer.CurrentModelTab);

	const modelIcon = process.env.PUBLIC_URL + '/static/models.svg';
	const conIcon = process.env.PUBLIC_URL + '/static/cons.svg';
	const rulesIcon = process.env.PUBLIC_URL + '/static/rules.svg';
	const dataStructuresIcon = process.env.PUBLIC_URL + '/static/dataStructures.svg';

	const items: TabsProps['items'] = [
		{
			key: '1',
			label: <Flex>
				<img src={modelIcon} alt="models" style={{ width: 25, paddingRight: '5px' }} />
				<span>Модели</span>
			</Flex>,
			children: <Models />,
		},
		{
			key: '2',
			label: <Flex >
				<img src={conIcon} alt="connectors" style={{ width: 25, paddingRight: '5px' }} />
				<span>Коннекторы</span>
			</Flex>,
			children: <Connectors />,
		},
		{
			key: '3',
			label: <Flex>
				<img src={rulesIcon} alt="rules" style={{ width: 25, paddingRight: '5px' }} />
				<span>Правила</span>
			</Flex>,
			children: <Rules />,
		},
		{
			key: '4',
			label: <Flex>
				<img src={dataStructuresIcon} alt="data structures" style={{ width: 25, paddingRight: '5px', }} />
				<span>Структуры данных</span>
			</Flex>,
			children: <DataStructureList />,
		},
	];

	return (
		<Tabs defaultActiveKey={currentTab ?? 1} items={items} style={{ width: "100%", padding: 20, maxHeight: '100vh' }} />
	);
};


export default ModelsManage;
