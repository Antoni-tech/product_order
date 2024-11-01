import React, { useState } from 'react'
import { Flex, Modal, Button } from 'antd'
import { PlusOutlined, MinusOutlined, SaveOutlined } from '@ant-design/icons';
import { useFormikContext } from 'formik';
import { useAppSelector } from '../../../../../redux/Store';
import { RulesController } from '../../../../../controllers/RulesController';
import { useDispatch } from 'react-redux';
import { IFieldRelation } from '../../../../../shared/entities/Rule/Rule';
import { useParams } from 'react-router-dom';
import { DataStructureController } from '../../../../../controllers/DataStructureController';
import SelectableStructure from './selectableStructure';



// interface MyFormValues {
// 	fields: { name: string }[];
// }
type MyFormValues = {
	addField: () => void
	saveFieldRelation: () => void
	removeField: (id: number) => void
};



const RulesDataStructureControl: React.FC<MyFormValues> = ({ addField, removeField, saveFieldRelation }) => {

	const formik = useFormikContext()
	const dispatch = useDispatch()

	const fieldsRelation = useAppSelector(state => state.RuleReducer.fieldRelation)
	const selectedFieldId = useAppSelector(state => state.RuleReducer.selectedField.id)


	const rulesController = new RulesController(dispatch)
	const { fromModel } = useParams()




	const SaveChanges = () => {
		rulesController.fieldRelation(fieldsRelation as IFieldRelation)
		// formik.handleSubmit()
		rulesController.getFieldRelation(Number(fromModel))
	}

	return (

		<Flex justify='space-between' style={{ width: '100%' }}>

			<Flex gap='small'>

				<Button type="primary" icon={<PlusOutlined />} onClick={addField} />

				<Button type="primary" icon={<MinusOutlined />}
					onClick={() => removeField(selectedFieldId)}
				/>

				{!fromModel && <SelectableStructure />}

			</Flex>

			{fromModel && <Button type="primary" icon={<SaveOutlined />} onClick={saveFieldRelation} />}

		</Flex>

	)
}

export default RulesDataStructureControl