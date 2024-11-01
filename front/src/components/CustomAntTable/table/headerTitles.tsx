import type { TableColumnsType } from 'antd';
import { DataModelTableType } from '../types';


export const inputColumns: TableColumnsType<DataModelTableType> = [
	{
		title: 'ID',
		dataIndex: 'id',
		key: 'id',
	},
	{
		title: 'Name',
		dataIndex: 'name',
		key: 'name',
		render: (text: string) => <a href='#'>{text}</a>,
	},
	{
		title: 'Запуск 2 этапа',
		dataIndex: 'launchSecondStage',
		key: 'launchSecondStage',
	},
	{
		title: 'Счетчик',
		dataIndex: 'count',
		key: 'count',
	},
	{
		title: 'Ошибки',
		dataIndex: 'error',
		key: 'error',
	},
	{
		title: 'Состояние',
		dataIndex: 'state',
		key: 'state',
	},
	{
		title: 'Время посл операции',
		dataIndex: 'time',
		key: 'time',
	}
]

export const resultColumns: TableColumnsType<DataModelTableType> = [
	{
		title: 'ID',
		dataIndex: 'id',
		key: 'id',
	},
	{
		title: 'Name',
		dataIndex: 'name',
		key: 'name',
	},
	{
		title: 'Тип результата',
		dataIndex: 'ruleType',
		key: 'ruleType',
	},
	{
		title: 'Очередность',
		dataIndex: 'queueNumber',
		key: 'queueNumber',
	},
	{
		title: 'Счетчик',
		dataIndex: 'count',
		key: 'count',
	},
	{
		title: 'Ошибки',
		dataIndex: 'error',
		key: 'error',
	},
	{
		title: 'Состояние',
		dataIndex: 'state',
		key: 'state',
	},
	{
		title: 'Время посл операции',
		dataIndex: 'time',
		key: 'time',
	}
]

export const outputColumns: TableColumnsType<DataModelTableType> = [
	{
		title: 'ID',
		dataIndex: 'id',
		key: 'id',
	},
	{
		title: 'Name',
		dataIndex: 'name',
		key: 'name',
	},
	{
		title: 'Счетчик',
		dataIndex: 'count',
		key: 'count',
	},
	{
		title: 'Ошибки',
		dataIndex: 'error',
		key: 'error',
	},
	{
		title: 'Состояние',
		dataIndex: 'state',
		key: 'state',
	},
	{
		title: 'Время посл операции',
		dataIndex: 'time',
		key: 'time',
	}
]