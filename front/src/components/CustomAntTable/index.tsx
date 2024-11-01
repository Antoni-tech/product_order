import React from 'react'
import { Flex } from 'antd'
import AntTableControl from './control'
import AntTable from './table/AntTable'
import { DataModelTableType } from './types'
import { IModelStructComponents } from '../../shared/entities/Connector/Connector'

type ICustomAntTable = {
	cols: { title: string, dataIndex: string, key: string }[] | null
	grid?: DataModelTableType[]
	dataSource: IModelStructComponents[] | undefined
	// fullData: any
}

const CustomAntTable: React.FC<ICustomAntTable> = ({ cols, grid, dataSource }) => {


	return (
		<Flex vertical gap='middle' style={{ padding: '8px', border: '1px solid black', borderRadius: 8 }}>
			{/* <AntTableControl fullData={fullData} checkedValues={dataSource as IModelStructComponents[]} /> */}
			<AntTable cols={cols} dataSource={dataSource as IModelStructComponents[]} />

		</Flex>
	)
}

export default CustomAntTable