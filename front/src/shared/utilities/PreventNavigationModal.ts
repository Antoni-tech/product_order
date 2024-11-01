import {Dispatch} from "react"
import {ModalService} from "../../service/Modal/ModalService"

export const PreventNavigationModal = (dispatch:Dispatch<any>, navigationFunc:Function) => {
    const modalService = new ModalService(dispatch) 

    modalService.setModalData({
         onSubmit:() => {
             navigationFunc()
             modalService.deleteModalData()
         },
         isOpen: true,
         onOpen: () => {},
         onClose: () => modalService.deleteModalData(),
         modalTextData: ("Are you sure you want to close the form without saving the data?"),
         context: "action"
    })
}
