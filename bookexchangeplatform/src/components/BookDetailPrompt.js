import React from 'react';
import Popup from 'reactjs-popup';

export default function BookDetailPrompt(props) {

    const onCloseModal = () => {
        console.log('close modal');
        props.setBookDetail('');
      }

  return (
    <div>
      <Popup contentStyle={{width: "40%"}} 
        position="right center"
        trigger={<button type="button" className="btn btn-link">{props.bookId}</button>} >
        <button type="button" className="btn btn-link" onClick={() => props.handleBookDetail(props.bookId)}>Fetch Book details</button>
        <table className="table table-striped">
            <thead>
                <tr>
                    {/* <th>Book Id</th> */}
                    <th>Title</th>
                    <th>Author</th>
                    <th>Genre</th>
                    <th>Condition</th>
                    <th>Publisher</th>
                </tr>   
            </thead>
            <tbody>
                <tr>
                {/* <td>
                    {props.bookDetail.id}
                </td> */}
                <td>
                    {props.bookDetail.title}
                </td>
                <td>
                    {props.bookDetail.author}
                </td>
                <td>
                    {props.bookDetail.genre}
                </td>
                <td>
                    {props.bookDetail.bookCondition}
                </td>
                <td>
                    {props.bookDetail.publisher}
                </td>
                </tr>
            </tbody>
        </table>
        <button type="button" className="btn btn-secondary" data-dismiss="modal" onClick={()=>onCloseModal()}>Clear</button>
      </Popup>
    </div>
  )
}
