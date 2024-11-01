import { Table } from 'antd'

import { IModelStructComponents } from '../../../shared/entities/Connector/Connector'
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../../redux/Model/ModelReducer';

import styles from './AntTable.module.scss'
import { useAppSelector } from '../../../redux/Store';
import { Flex } from 'antd'

function AntTable({ cols, dataSource }: { cols: any, dataSource: IModelStructComponents[] }) {

	const dispatch = useDispatch()

	const selectedID = useAppSelector(store => store.ModelReducer.selectedModelField?.modelComponentId)


	return (
		<Table
			style={{ maxWidth: '100%', height: '100%' }}
			scroll={{ y: 240 }}
			showHeader={false}
			columns={cols}
			dataSource={dataSource}
			rowKey={(dataSource: IModelStructComponents) => dataSource.modelComponentId}
			pagination={false}
			size='small'
			rowClassName={(record) => (record.modelComponentId === selectedID ? styles.picked : styles.default)}
			onRow={(record) => {
				return {
					onClick: () => {
						dispatch({ type: ActionTypes.MODEL_SELECT_FIELD, payload: record })
					},
				};
			}}
			expandable={{
				expandedRowRender: (record) =>
					<Flex style={{ margin: 0 }} vertical>
						<span>id: {record.modelComponentId}</span>
						<span>Название: {record.name}</span>
						<span>Создал: {record.userName}</span>
						{
							record.type === 'CONNECTOR_INPUT' ? <span>Внешние системы: QPragma, OpenWay, IPSO</span> :
								record.type === 'RULE' ? <span>Цель: Выявление отклонений</span> :
									<span>Внешние системы: www.test.kz</span>
						}
					</Flex>,
				expandRowByClick: true, // Добавляем это свойство
				expandIcon: ({ expanded, onExpand, record }) => null,
				showExpandColumn: false
			}}
		/>

	)
}

export default AntTable