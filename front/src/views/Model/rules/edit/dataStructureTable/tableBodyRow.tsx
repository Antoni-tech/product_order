import React from 'react';

import { Row, Col } from 'antd'
import {
	SimpleTreeItemWrapper,
	TreeItemComponentProps,
} from "dnd-kit-sortable-tree";
import { useDispatch } from 'react-redux';



const RulesDataStructureTableBodyRow = React.forwardRef<
	HTMLDivElement,
	TreeItemComponentProps<any>
>((props, ref) => {

	const dispatch = useDispatch()

	return (

		<SimpleTreeItemWrapper {...props} ref={ref}>

			<Row style={{ width: '800px', 'marginLeft': 'auto' }} onClick={() => dispatch({ type: 'RULE_SELECT_FIELD', payload: props.item.id })} >
				<Col span={16}>
					<span>{props.item.name}</span>
				</Col>
				<Col span={8}>
					<span>{props.item.id}</span>
				</Col>
			</Row>


		</SimpleTreeItemWrapper>


	);
});

export default RulesDataStructureTableBodyRow