import React, { useMemo } from 'react'
import { useAppSelector } from '../../../../redux/Store';
import { Link } from 'react-router-dom';

function ModelLinkButton({ link, id, text }: { link: string, id: number, text: string }) {

	const fromModel = useAppSelector(state => state.ModelReducer.model?.versionId);

	const modelId = useMemo(() => {
		// Используйте значение fromModel для вычисления modelId
		return fromModel ? fromModel : null;
	}, [fromModel]);

	return (
		<Link to={`${link}/${id}/${modelId}`}>
			{text}
		</Link>
	)
}
// 
export default ModelLinkButton