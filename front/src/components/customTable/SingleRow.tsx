import React, { useState } from 'react';
import { DataType } from './types';

import { Row, Col, Popover } from 'antd'
import {
	SimpleTreeItemWrapper,
	TreeItemComponentProps,
} from "dnd-kit-sortable-tree";

import InputItem from './tableItems/InputItem';
import SelectItem from './tableItems/SelectItem';
import CheckBoxItem from './tableItems/CheckBoxItem';
import NumberInput from './tableItems/NumberInput';
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../redux/Model/ConnectorInputReducer';
import { useAppSelector } from '../../redux/Store';
import LinkBtn from './tableItems/LinkItem';

const SingleRowTreeItem = React.forwardRef<
	HTMLDivElement,
	TreeItemComponentProps<DataType>
>((props, ref) => {

	const dispatch = useDispatch()
	const PickedId = useAppSelector(store => store.ConnectorInputReducer.fieldResponse?.id);
	const OutPickedId = useAppSelector(store => store.ConnectorOutputReducer.fieldResponse?.id);
	const [isPick, setPick] = useState(false)

	const content = (
		<div>
			<p>Content</p>
		</div>
	);


	const pickField = () => {
		setPick(true)
		dispatch({
			type: ActionTypes.CONNECTOR_PICK_ROW,
			payload: {
				field: props.item.id
			}
		})
	}

	const unpickField = () => {
		setPick(false)
		dispatch({
			type: ActionTypes.CONNECTOR_UNPICK_ROW,
			payload: {
				field: props.item.id
			}
		})
	}

	return (

		<SimpleTreeItemWrapper {...props} ref={ref} >

			<Row style={{ width: '930px', 'marginLeft': 'auto', position: 'relative' }} onClick={isPick === true ? unpickField : pickField} >

				{(PickedId || OutPickedId) === props.item.id &&
					<span style={{ borderRadius: '100px', width: 10, height: 10, position: 'absolute', left: '-15px', top: '50%', transform: 'translateY(-50%)', backgroundColor: 'green' }}></span>
				}

				<Col span={4}>


					<InputItem
						defaultValue={props.item.name}
						id={props.item.id}
						type={props.item.type}
						newItem={props.item.newItem}
						pickField={pickField}
					/>


				</Col>
				<Col span={4}>
					<SelectItem defaultValue={props.item.fieldType} id={props.item.id} type={props.item.type} children={props.item.children ? true : false} />
				</Col>


				<Col span={4}>
					{props.item.fieldType !== 'OBJECT' &&
						<NumberInput defaultValue={props.item.maxSize} id={props.item.id} fieldId={'maxSize'} type={props.item.type} />
					}
				</Col>
				<Col span={3}>
					{props.item.fieldType !== 'OBJECT' &&
						<CheckBoxItem defaultValue={props.item.allowEmpty} id={props.item.id} fieldId={0} type={props.item.type} />
					}
				</Col>
				<Col span={3}>
					{props.item.fieldType !== 'OBJECT' &&
						<CheckBoxItem defaultValue={props.item.prohibitSpecCharacters} id={props.item.id} fieldId={1} type={props.item.type} />
					}
				</Col>
				<Col span={3}>
					{props.item.fieldType !== 'OBJECT' &&
						<CheckBoxItem defaultValue={props.item.allowArray} id={props.item.id} fieldId={2} type={props.item.type} />
					}
				</Col>
				<Col span={3}>
					{props.item.fieldType !== 'OBJECT' &&
						<NumberInput defaultValue={props.item.maxArray} id={props.item.id} fieldId={'maxArray'} type={props.item.type} />
					}
				</Col>
			</Row>


		</SimpleTreeItemWrapper>


	);
});

export default SingleRowTreeItem