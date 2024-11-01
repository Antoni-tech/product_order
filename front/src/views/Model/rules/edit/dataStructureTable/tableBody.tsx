import { useEffect, useState } from 'react'
import {
	SortableTree,
	TreeItems
} from "dnd-kit-sortable-tree";
import RulesDataStructureTableBodyRow from './tableBodyRow';
import { useFormikContext } from 'formik';
import { Empty } from 'antd'

type DataType = {
	name: string,
	id: number;
}

type IFormik = {
	fields: DataType[]
}

// interface MyFormValues {
// 	fields: { name: string }[];
// }
type MyFormValues = {
	fields?: { name: string, id: number; }[];
};

const RulesDataStructureTableBody: React.FC<MyFormValues> = ({ fields }) => {

	const [data, setData] = useState<TreeItems<DataType>>([]);
	const { values } = useFormikContext<IFormik>();
	const formik = useFormikContext<MyFormValues>()

	useEffect(() => {

		// values.fields !== null && values.fields !== undefined ?
		// setData(values.fields.map((field) => ({ ...field }))) :
		fields !== null && fields !== undefined ?
			setData(fields.map((field: { name: string, id: number }) => ({ ...field }))) :
			setData([])

	}, [fields]);

	return (



		<>
			{/* {values.fields !== null && values.fields !== undefined ? */}
			{fields !== null && fields !== undefined ?
				<SortableTree
					items={data}
					onItemsChanged={(newItems, reason) => {
						setData(newItems);
						formik.setFieldValue("fields", newItems);
					}}
					TreeItemComponent={RulesDataStructureTableBodyRow}
				/>
				:
				<Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
			}
		</>
	)
}

export default RulesDataStructureTableBody