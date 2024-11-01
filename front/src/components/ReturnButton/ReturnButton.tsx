import { faArrowLeft, faArrowUp } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { FloatButton } from 'antd'
import { useNavigate } from 'react-router-dom'
import { URLPaths } from '../../config/application/URLPaths'
import { useDispatch } from 'react-redux'
import { ActionTypes } from '../../redux/Model/ModelReducer'

function ReturnButton({ tab, fromModel }: { tab: string, fromModel?: string }) {

	const navigate = useNavigate()
	const dispatch = useDispatch()

	return (
		<FloatButton
			shape="square"
			style={{ right: 24, bottom: 90 + 24 }}
			icon={<FontAwesomeIcon icon={faArrowLeft} />}
			onClick={() => {
				dispatch({
					type: ActionTypes.GENERAL_CHANGE_MODEL_TAB,
					payload: tab
				})
				navigate(!fromModel ? URLPaths.MODELS.link : `${URLPaths.MODEL_EDIT.link}/${fromModel}`)
			}}
		/>
	)
}

export default ReturnButton