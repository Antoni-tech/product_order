import { useEffect, useState } from 'react'
import {
	SortableTree,
	TreeItems
} from "dnd-kit-sortable-tree";

import { DataType } from './types';
import SingleRowTreeItem from './SingleRow';
import { Spin } from 'antd'

function SortableTreeTable({ data, updatePos, isInputConnector }: { data: DataType[], updatePos: (value: DataType[]) => void, isInputConnector: boolean }) {

	const [items, setItems] = useState<TreeItems<DataType>>(data);
	const [loading, setLoading] = useState(false)


	useEffect(() => {
		setLoading(true)
		setItems(data.map((field) => ({
			...field, type: isInputConnector ? 'input' : 'output'
		})))
		setLoading(false);
	}, [data]);



	return (
		<>
			{
				loading ?
					<Spin /> :
					<SortableTree
						items={items}
						onItemsChanged={(newItems, reason) => {
							setItems(newItems);
							updatePos(newItems)
						}}
						TreeItemComponent={SingleRowTreeItem}
					/>
			}
		</>



	);
}


export default SortableTreeTable