import React from 'react';
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';

export default function Prompt(props) {
  return (
    <div>
      <Popup trigger={<button type="button" className="btn btn-info btn-sm mx-1"> Borrow </button>}
            position="right center">
            <div className='borrow-book-container'>
                <div>Borrow Book</div>
                <form>
                    <table className="table table-hover table-sm">
                        <tbody>
                            <tr>
                                <th scope="col">Duration (in days)</th>
                                <td><input type="number" id="title" className="form-control" min={1}
                                value={props.duration} onChange={(event) => props.handleInputChange(event, props.setDuration)} required/></td>
                            </tr>
                            <tr>
                                <th scope="col">Delivery Mode</th>
                                <td>
                                <select className="form-select" aria-label="Default select example"
                                value={props.deliveryMode} 
                                onChange={(event) => props.handleInputChange(event, props.setDeliveryMode)}
                                >
                                <option value='' disabled>Open this select menu</option>
                                <option value="STANDARD">STANDARD</option>
                                <option value="EXPRESS">EXPRESS</option>
                                </select>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <button type="button" className="btn btn-primary" onClick={() => props.handleBookBorrow(props.bookIdForBorrow)}>Raise Borrow Request</button>
                </form>
            </div>
    </Popup>
    </div>
  )
}
